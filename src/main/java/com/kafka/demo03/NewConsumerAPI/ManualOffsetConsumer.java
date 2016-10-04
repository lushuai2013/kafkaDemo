package com.kafka.demo03.NewConsumerAPI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 示例1：批量提交偏移量
 * 手动批量提交偏移量
 *
使用newConsumer API 只需要引用kafka-clients即可
 newConsumer API 更加易懂、易用
 <dependency>
 <groupId>org.apache.kafka</groupId>
 <artifactId>kafka-clients</artifactId>
 <version>0.10.0.0</version>
 </dependency>

 * Created by lushuai on 16-10-5.
 */
public class ManualOffsetConsumer {
    private static Logger LOG = LoggerFactory.getLogger(ManualOffsetConsumer.class);
    public ManualOffsetConsumer() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Properties props = new Properties();
        //props.put("bootstrap.servers", bootstrapServers);//"172.16.49.173:9092;172.16.49.173:9093");
        //设置brokerServer(kafka)ip地址
        props.put("bootstrap.servers", "localhost:9092");
        //设置consumer group name
        props.put("group.id","manual_g1");

        props.put("enable.auto.commit", "false");

        //设置使用最开始的offset偏移量为该group.id的最早。如果不设置，则会是latest即该topic最新一个消息的offset
        //如果采用latest，消费者只能得道其启动后，生产者生产的消息
        props.put("auto.offset.reset", "earliest");
        //
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String ,String> consumer = new KafkaConsumer<String ,String>(props);
        consumer.subscribe(Arrays.asList("producer_test"));
        final int minBatchSize = 5;  //批量提交数量
        List<ConsumerRecord<String, String>> buffer = new ArrayList<ConsumerRecord<String, String>>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("consumer message values is "+record.value()+" and the offset is "+ record.offset());
                buffer.add(record);
            }
            if (buffer.size() >= minBatchSize) {
                LOG.info("now commit offset");
                consumer.commitSync();
                buffer.clear();
            }
        }
    }
}
