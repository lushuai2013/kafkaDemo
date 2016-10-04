package com.kafka.demo01.newapi;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * Created by lushuai on 16-10-3.
 */
public class ProducerDemoNewApi {
    public static void main(String[] args) {
        Random rnd = new Random();
        int events=200;

        // 设置配置属性
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092,localhost:9093,localhost:9094");
        // key.serializer默认为serializer.class
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // key.serializer默认为serializer.class
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 可选配置，如果不配置，则使用默认的partitioner
        props.put("partitioner.class", "com.kafka.demo01.PartitionerDemo");
        // 触发acknowledgement机制，否则是fire and forget，可能会引起数据丢失
        // 值为0,1,-1,可以参考
        // http://kafka.apache.org/08/configuration.html
        props.put("acks", "1");

        // 创建producer
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        // 产生并发送消息
        long start=System.currentTimeMillis();
        for (long i = 100; i < events; i++) {
            long runtime = new Date().getTime();
            String ip = "192.168.2." + i;//rnd.nextInt(255);
            String msg = runtime + ",www.example.com," + ip;
            //如果topic不存在，则会自动创建，默认replication-factor为1，partitions为0
            ProducerRecord<String, String> data = new ProducerRecord<String, String>("page_visits", ip, msg);
            producer.send(data);
        }
        System.out.println("耗时:" + (System.currentTimeMillis() - start));
        // 关闭producer
        producer.close();
    }
}
