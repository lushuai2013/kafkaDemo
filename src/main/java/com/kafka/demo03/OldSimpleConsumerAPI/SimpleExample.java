package com.kafka.demo03.OldSimpleConsumerAPI;
import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.OffsetAndMetadata;
import kafka.common.OffsetMetadata;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import java.nio.ByteBuffer;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by lushuai on 16-10-4.
 */
public class SimpleExample {
    private static Logger LOG = LoggerFactory.getLogger(SimpleExample.class);

    private static String group_name="producer-test-group-1";
    private static String clientName;
    private static long initialOffset;

    public static void main(String args[]) {
        SimpleExample example = new SimpleExample();
       /* Scanner sc = new Scanner(System.in);
        System.out.println("请输入broker节点的ip地址(如172.16.49.173)");
        String brokerIp = sc.nextLine();
        List<String> seeds = new ArrayList<String>();
        seeds.add(brokerIp);
        System.out.println("请输入broker节点端口号(如9092)");
        int port = Integer.parseInt( sc.nextLine());
        System.out.println("请输入要订阅的topic名称(如test)");
        String topic = sc.nextLine();
        System.out.println("请输入要订阅要查找的分区(如0)");
        int partition = Integer.parseInt( sc.nextLine());
        System.out.println("请输入最大读取消息数量(如10000)");*/
        String brokerIp = "localhost";
        List<String> seeds = new ArrayList<String>();
        seeds.add(brokerIp);
        int port = 9092;
        String topic ="producer_test";

        int partition = 0;
        long maxReads = 10l; //最大读取消息数量

        try {
            System.out.println("@SimpleExample start..");
            example.run(maxReads, topic, partition, seeds, port);
        } catch (Exception e) {
            LOG.error("Oops:" + e);
            e.printStackTrace();
        }
    }

    private List<String> m_replicaBrokers = new ArrayList<String>();

    public SimpleExample() {
        m_replicaBrokers = new ArrayList<String>();
    }

    public void run(long a_maxReads, String a_topic, int a_partition, List<String> a_seedBrokers, int a_port) throws Exception {
        // find the meta data about the topic and partition we are interested in
        //获取指定Topic partition的元数据

        PartitionMetadata metadata = findLeader(a_seedBrokers, a_port, a_topic, a_partition);
        if (metadata == null) {
            LOG.error("Can't find metadata for Topic and Partition. Exiting");
            return;
        }
        if (metadata.leader() == null) {
            LOG.error("Can't find Leader for Topic and Partition. Exiting");
            return;
        }
        String leadBroker = metadata.leader().host();
        clientName = "Client_" + a_topic + "_" + a_partition;
        SimpleConsumer consumer = new SimpleConsumer(leadBroker, a_port, 100000, 64 * 1024, clientName);
        long readOffset = getLastOffset(consumer,a_topic, a_partition, kafka.api.OffsetRequest.EarliestTime(), clientName);

        int numErrors = 0;
        while (a_maxReads > 0) {
            if (consumer == null) {
                consumer = new SimpleConsumer(leadBroker, a_port, 100000, 64 * 1024, clientName);
            }
            FetchRequest req = new FetchRequestBuilder()
                    .clientId(clientName)
                    .addFetch(a_topic, a_partition, readOffset, 100000) // Note: this fetchSize of 100000 might need to be increased if large batches are written to Kafka
                    .build();
            FetchResponse fetchResponse = consumer.fetch(req);

            if (fetchResponse.hasError()) {
                numErrors++;
                // Something went wrong!
                short code = fetchResponse.errorCode(a_topic, a_partition);
                LOG.error("Error fetching data from the Broker:" + leadBroker + " Reason: " + code);
                if (numErrors > 5) break;
                if (code == ErrorMapping.OffsetOutOfRangeCode())  {
                    // We asked for an invalid offset. For simple case ask for the last element to reset
                    readOffset = getLastOffset(consumer,a_topic, a_partition, kafka.api.OffsetRequest.LatestTime(), clientName);
                    continue;
                }
                consumer.close();
                consumer = null;
                leadBroker = findNewLeader(leadBroker, a_topic, a_partition, a_port);
                continue;
            }
            numErrors = 0;

            long numRead = 0;
            for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(a_topic, a_partition)) {
                long currentOffset = messageAndOffset.offset();
                if (currentOffset < readOffset) {
                    LOG.error("Found an old offset: " + currentOffset + " Expecting: " + readOffset);
                    continue;
                }
                readOffset = messageAndOffset.nextOffset();
                ByteBuffer payload = messageAndOffset.message().payload();

                byte[] bytes = new byte[payload.limit()];
                payload.get(bytes);
                System.out.println("partition is "+a_partition+" the messag's offset is :"+String.valueOf(messageAndOffset.offset()) + " and the value is :" + new String(bytes, "UTF-8"));
                numRead++;
                a_maxReads--;
            }
            if(numRead>0){
                commitOffsets(consumer,a_topic,a_partition,readOffset);
            }

            if (numRead == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
        if (consumer != null) consumer.close();
    }

    public static long getLastOffset(SimpleConsumer consumer, String topic, int partition,
                                     long whichTime, String clientName) {
        TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
        kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(
                requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
        OffsetResponse response = consumer.getOffsetsBefore(request);

        if (response.hasError()) {
            LOG.error("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition) );
            return 0;
        }
        long[] offsets = response.offsets(topic, partition);
        return offsets[0];
    }

    /**
     * 参考http://www.programcreek.com/java-api-examples/index.php?api=kafka.javaapi.OffsetCommitResponse
     * @param consumer
     * @param topic
     * @param partition
     * @param lastReturnedOffset
     * @return
     */
    public static boolean commitOffsets(SimpleConsumer consumer,String topic, int partition,long lastReturnedOffset){

        long now = System.currentTimeMillis();
        Map<TopicAndPartition, OffsetAndMetadata> offsets = new LinkedHashMap<TopicAndPartition, OffsetAndMetadata>();
        TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
        offsets.put(topicAndPartition, new OffsetAndMetadata(new OffsetMetadata(lastReturnedOffset,"meta data test"), now,now));
        //offsets.put(testPartition1, new OffsetAndMetadata(200L, "more metadata", now));
        OffsetCommitRequest commitRequest = new OffsetCommitRequest(
                group_name,
                offsets,
                0,
                clientName,
                (short) 1 /* version */); // version 1 and above commit to Kafka, version 0 commits to ZooKeeper

        OffsetCommitResponse commitResponse = consumer.commitOffsets(commitRequest);
        if (commitResponse.hasError()) {
            LOG.error("Error encountered whilst committing offset " + lastReturnedOffset + " for Kafka");
            LOG.error("Commit error code: " + commitResponse.errorCode(new TopicAndPartition(topic, 0)));
            return false;
        } else {
            // update offset from which to read on next request
            LOG.debug("Consumer commit: Updating initial offset from "
                    + initialOffset + " to " + lastReturnedOffset);
            initialOffset = lastReturnedOffset;
            LOG.debug("Consumer has committed offset " + lastReturnedOffset + " for topic " + topic);
            return true;
        }

    }
    /**
     * 找一个leader broker
     * 遍历每个broker，取出该topic的metadata，然后再遍历其中的每个partition metadata，如果找到我们要找的partition就返回
     * 根据返回的PartitionMetadata.leader().host()找到leader broker
     * @param a_oldLeader
     * @param a_topic
     * @param a_partition
     * @param a_port
     * @return
     * @throws Exception
     */
    private String findNewLeader(String a_oldLeader, String a_topic, int a_partition, int a_port) throws Exception {
        for (int i = 0; i < 3; i++) {
            boolean goToSleep = false;
            PartitionMetadata metadata = findLeader(m_replicaBrokers, a_port, a_topic, a_partition);
            if (metadata == null) {
                goToSleep = true;
            } else if (metadata.leader() == null) {
                goToSleep = true;
            } else if (a_oldLeader.equalsIgnoreCase(metadata.leader().host()) && i == 0) {
                // first time through if the leader hasn't changed give ZooKeeper a second to recover
                // second time, assume the broker did recover before failover, or it was a non-Broker issue
                //
                goToSleep = true;
            } else {
                return metadata.leader().host();
            }
            if (goToSleep) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
        LOG.error("Unable to find new leader after Broker failure. Exiting");
        throw new Exception("Unable to find new leader after Broker failure. Exiting");
    }
    /**
     *
     * @param a_seedBrokers
     * @param a_port
     * @param a_topic
     * @param a_partition
     * @return
     */
    private PartitionMetadata findLeader(List<String> a_seedBrokers, int a_port, String a_topic, int a_partition) {
        PartitionMetadata returnMetaData = null;

        loop:
        for (String seed : a_seedBrokers) { //遍历每个broker
            SimpleConsumer consumer = null;
            try {
                // 创建Simple Consumer，
                consumer = new SimpleConsumer(seed, a_port, 100000, 64 * 1024, "leaderLookup");
                List<String> topics = Collections.singletonList(a_topic);
                TopicMetadataRequest req = new TopicMetadataRequest(topics);
                //发送TopicMetadata Request请求
                kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);
                //取到Topic的Metadata
                List<TopicMetadata> metaData = resp.topicsMetadata();
                //遍历每个partition的metadata
                for (TopicMetadata item : metaData) {
                    for (PartitionMetadata part : item.partitionsMetadata()) {
                        // 判断是否是要找的partition
                        if (part.partitionId() == a_partition) {
                            returnMetaData = part;
                            //找到就返回
                            break loop;
                        }
                    }
                }
            } catch (Exception e) {
                LOG.info("Error communicating with Broker [" + seed + "] to find Leader for [" + a_topic
                        + ", " + a_partition + "] Reason: " + e);
            } finally {
                if (consumer != null) consumer.close();
            }
        }
        if (returnMetaData != null) {
            m_replicaBrokers.clear();
            for (kafka.cluster.BrokerEndPoint replica : returnMetaData.replicas()) {
                m_replicaBrokers.add(replica.host());
            }
        }
        return returnMetaData;
    }
}
