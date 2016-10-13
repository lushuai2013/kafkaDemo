package com.kafka.demo04.newapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by lushuai on 16/10/13.
 */
public class IndexReulstConsumer {
    private  final Logger LOG = LoggerFactory.getLogger(IndexReulstConsumer.class);

    public IndexReulstConsumer() {
    }

    public void consumerStartRun(){
        int numConsumers = 3;
//        String groupName = "consumer-indexCal-group";
//        List<String> topics = Arrays.asList("index-calculate-topic");
        String groupName = "group01";
        List<String> topics = Arrays.asList("test-topic01");
        final ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

        final List<ConsumerRunnable> consumers = new ArrayList<>();
        ConsumerRunnable consumer = new ConsumerRunnable(topics,groupName);
        consumers.add(consumer);
        executor.submit(consumer);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (ConsumerRunnable consumer : consumers) {
                    consumer.shutdown();
                }
                executor.shutdown();
                try {
                    executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        new IndexReulstConsumer().consumerStartRun();
    }
}
