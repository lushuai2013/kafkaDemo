package com.kafka.demo03.NewConsumerAPI;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**消费指定分区的消息
 *
 * 使用newConsumer API 只需要引用kafka-clients即可
    newConsumer API 更加易懂、易用
     <dependency>
     <groupId>org.apache.kafka</groupId>
     <artifactId>kafka-clients</artifactId>
     <version>0.10.0.0</version>
     </dependency>
 * Created by lushuai on 16-10-5.
 */
public class ManualPartion {
    private static Logger LOG = LoggerFactory.getLogger(ManualPartion.class);
    public ManualPartion() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        //设置brokerServer(kafka)ip地址
        props.put("bootstrap.servers", "localhost:9092");
        //设置consumer group name
        props.put("group.id", "manual_g4");
        //设置自动提交偏移量(offset),由auto.commit.interval.ms控制提交频率
        props.put("enable.auto.commit", "true");
        //偏移量(offset)提交频率
        props.put("auto.commit.interval.ms", "1000");
        //设置使用最开始的offset偏移量为该group.id的最早。如果不设置，则会是latest即该topic最新一个消息的offset
        //如果采用latest，消费者只能得道其启动后，生产者生产的消息
        props.put("auto.offset.reset", "earliest");
        //
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        TopicPartition partition0 = new TopicPartition("producer_test", 0);
        TopicPartition partition1 = new TopicPartition("producer_test", 1);
        KafkaConsumer<String ,String> consumer = new KafkaConsumer<String ,String>(props);
        consumer.assign(Arrays.asList(partition0, partition1));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s  \r\n", record.offset(), record.key(), record.value());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
