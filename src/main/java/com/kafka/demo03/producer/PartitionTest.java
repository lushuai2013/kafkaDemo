package com.kafka.demo03.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Properties;
/**
 *
 *
 * 第一步：使用./kafka-topics.sh 命令创建topic及partitions 分区数
 ./kafka-topics.sh --create --zookeeper localhost:2181 --topic producer_test --partitions 10 --replication-factor 3

 备注： 要先用命令创建topic及partitions 分区数;否则在自定义的分区中如果有大于1的情况下，发送数据消息到kafka时会报expired due to timeout while requesting metadata from brokers错误

 * Created by lushuai on 16-10-4.
 */
public class PartitionTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092;localhost:9093;localhost:9094");

        props.put("retries", 0);
        // props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        // props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("partitioner.class", "com.kafka.demo03.producer.MyPartition");
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        int events=10;
        for (long i = 0; i < events; i++) {
            long runtime = new Date().getTime();
            String ip = "192.168.2." + i;
            String msg = runtime + ",www.example," + ip;
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("producer_test",ip,msg);
            producer.send(record);

           /* producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    // TODO Auto-generated method stub
                    if (e != null)
                        LOG.error("the producer has a error:" + e.getMessage());
                    else {
                        LOG.info("The offset of the record we just sent is: " + metadata.offset());
                        LOG.info("The partition of the record we just sent is: " + metadata.partition());
                    }

                }
            });*/
        }

        try {
            Thread.sleep(1000);
            producer.close();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}