package com.kafka.demo04.newapi;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lushuai on 16/10/13.
 */
public class ConsumerRunnable implements Runnable{
    private  final Logger LOG = LoggerFactory.getLogger(ConsumerRunnable.class);

    private final KafkaConsumer<String, String> consumer;
    private final List<String> topics;

    public ConsumerRunnable(List<String> topics,String groupName) {
        this.topics = topics;
        Properties props=createConsumerConfig(groupName);
        this.consumer = new KafkaConsumer<>(props);
    }
    /**
     * consumer 配置.
     * @return
     */
    private static Properties createConsumerConfig(String groupName) {
        Properties props = new Properties();
        //props.put("bootstrap.servers", "10.143.90.22:6667,10.143.90.23:6667");
        props.put("bootstrap.servers", "localhost:9092;localhost:9093;localhost:9094");
        props.put("group.id", groupName);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        return props;
    }
    @Override
    public void run() {
        try {
            LOG.info("ConsumerRunnable start.");
            Map<String, List<PartitionInfo>> topicsList = consumer.listTopics();
            for(Map.Entry<String, List<PartitionInfo>> entry : topicsList.entrySet()) {
                String key = entry.getKey();
                LOG.info("key:{}",key);
                List<PartitionInfo> pList = entry.getValue();
                for(PartitionInfo p:pList){
                    //LOG.info("p.partition:{},p.replicas:{},p.topic:{},p.toString:{}",p.partition(),p.replicas(),p.topic(),p.toString());
                }

            }
            consumer.subscribe(topics);
            while (true) {
                LOG.info("ConsumerRunnable start 1.");
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                LOG.info("ConsumerRunnable start 2.");
                for (ConsumerRecord<String, String> record : records) {
                    LOG.info("ConsumerRunnable start 3.");
                    Map<String, Object> data = new HashMap<>();
                    data.put("partition", record.partition());
                    data.put("offset", record.offset());
                    data.put("value", record.value());
                    System.out.println(String.format("partition:%s,offset:%s,value:%s",
                            record.partition(),record.offset(),record.value()));
                    print(record.value());
                }
            }
        } catch (WakeupException e) {
            LOG.error("ConsumerRunnable error:{}",e.getMessage());
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }

    public void print(String jsonStr){
        Map<String, Object> map = JosnHelper.jsonToMap(jsonStr);

        System.out.println(String.format("key:app_no,value:%s",map.get("app_no")));
        System.out.println(String.format("key:channel,value:%s",map.get("channel")));
        //renfa
        Map<String, Object> mapRenfa = (Map<String, Object>)map.get("renfa");
        System.out.println(String.format("key:code,value:%s",mapRenfa.get("code")));
        System.out.println(String.format("key:message,value:%s",mapRenfa.get("message")));
        Map<String, Object> indexsRenfa = (Map<String, Object>)mapRenfa.get("indexs");
        System.out.println(String.format("key:RFCN010001,value:%s",indexsRenfa.get("RFCN010001")));
        System.out.println(String.format("key:RFCN010002,value:%s",indexsRenfa.get("RFCN010002")));
        System.out.println(String.format("key:RFCN010003,value:%s",indexsRenfa.get("RFCN010003")));
        System.out.println(String.format("key:RFCN010004,value:%s",indexsRenfa.get("RFCN010004")));
        System.out.println(String.format("key:RFCN010005,value:%s",indexsRenfa.get("RFCN010005")));
        //shixin
        Map<String, Object> mapShixin = (Map<String, Object>)map.get("shixin");
        System.out.println(String.format("key:code,value:%s",mapShixin.get("code")));
        System.out.println(String.format("key:message,value:%s",mapShixin.get("message")));
        Map<String, Object> indexsShixin = (Map<String, Object>)mapShixin.get("indexs");
        System.out.println(String.format("key:SXCN010001,value:%s",indexsShixin.get("SXCN010001")));
        System.out.println(String.format("key:SXCN010002,value:%s",indexsShixin.get("SXCN010002")));

        //bop
        Map<String, Object> mapBop = (Map<String, Object>)map.get("bop");
        System.out.println(String.format("key:code,value:%s",mapBop.get("code")));
        System.out.println(String.format("key:message,value:%s",mapBop.get("message")));
        Map<String, Object> indexsBop = (Map<String, Object>)mapBop.get("indexs");
        System.out.println(String.format("key:PBCR0D0041,value:%s",indexsBop.get("PBCR0D0041")));
        System.out.println(String.format("key:PBCR0D0004,value:%s",indexsBop.get("PBCR0D0004")));

    }
}
