package com.kafka.demo03.NewConsumerAPI;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 *
 * 需要注意的:

 group.id :必须设置
 auto.offset.reset：如果想获得消费者启动前生产者生产的消息，则必须设置为earliest；如果只需要获得消费者启动后生产者生产的消息，则不需要设置该项
 enable.auto.commit(默认值为true)：如果使用手动commit offset则需要设置为false，并再适当的地方调用consumer.commitSync()，否则每次启动消费折后都会从头开始消费信息(在auto.offset.reset=earliest的情况下);

 * 使用newConsumer API 只需要引用kafka-clients即可
 newConsumer API 更加易懂、易用
 <dependency>
 <groupId>org.apache.kafka</groupId>
 <artifactId>kafka-clients</artifactId>
 <version>0.10.0.0</version>
 </dependency>


 * Created by lushuai on 16-10-5.
 */
public class AutoCommitOffSet {

    private static String a_groupId="group-8";
    public static void main(String[] args) {
        Properties props = new Properties();
        //brokerServer(kafka)ip地址,不需要把所有集群中的地址都写上，可是一个或一部分
        props.put("bootstrap.servers", "localhost:9092");
        //设置consumer group name,必须设置
        props.put("group.id", a_groupId);
        //设置自动提交偏移量(offset),由auto.commit.interval.ms控制提交频率
        props.put("enable.auto.commit", "true");
        //偏移量(offset)提交频率
        props.put("auto.commit.interval.ms", "1000");
        //设置使用最开始的offset偏移量为该group.id的最早。如果不设置，则会是latest即该topic最新一个消息的offset
        //如果采用latest，消费者只能得道其启动后，生产者生产的消息
        props.put("auto.offset.reset", "earliest");
        //设置心跳时间
        props.put("session.timeout.ms", "30000");
        //设置key以及value的解析（反序列）类
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        //订阅topic
        consumer.subscribe(Arrays.asList("producer_test"));
        while (true) {
            //每次取100条信息
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.println(String.format("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value()));

        }

    }
}
