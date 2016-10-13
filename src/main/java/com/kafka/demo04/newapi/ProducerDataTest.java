package com.kafka.demo04.newapi;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by lushuai on 16/10/13.
 */
public class ProducerDataTest {
    static String jsonStr="{\n" +
            "    \"app_no\": \"%s\",\n" +
            "    \"channel\":\"meiyifen\",\n" +
            "    \"renfa\": {\n" +
            "        \"code\": 0,\n" +
            "        \"message\": \"³É¹¦\",\n" +
            "        \"indexs\": {\n" +
            "            \"RFCN010001\": 1,\n" +
            "            \"RFCN010002\": 1,\n" +
            "            \"RFCN010003\": \"2016-09-30\",\n" +
            "            \"RFCN010004\": \"2016-09-30\",\n" +
            "            \"RFCN010005\": 1,\n" +
            "            \"RFCN010006\": 1\n" +
            "        }\n" +
            "    },\n" +
            "    \"shixin\": {\n" +
            "        \"code\": 0,\n" +
            "        \"message\": \"³É¹¦\",\n" +
            "        \"indexs\": {\n" +
            "            \"SXCN010001\": 1,\n" +
            "            \"SXCN010002\": 1,\n" +
            "            \"SXCN010003\": \"2016-09-30\",\n" +
            "            \"SXCN010004\": \"2016-09-30\",\n" +
            "            \"SXCN010005\": 1,\n" +
            "            \"SXCN010006\": 1\n" +
            "        }\n" +
            "    },\n" +
            "    \"bop\": {\n" +
            "        \"code\": 0,\n" +
            "        \"message\": \"³É¹¦\",\n" +
            "        \"indexs\": {\n" +
            "            \"PBCR0D0041\": 1,\n" +
            "            \"PBCR0D0004\": 1,\n" +
            "            \"PBCR0Z0001\": 1,\n" +
            "            \"PBCR0D0005\": 1,\n" +
            "            \"PBCR0C0001\": 1,\n" +
            "            \"PBCR0D0052\": 1,\n" +
            "            \"PBCR0D0019\": 1,\n" +
            "            \"PBCR0Z0002\": 1,\n" +
            "            \"PBCR0D0053\": 1,\n" +
            "            \"PBCR0D0054\": 1,\n" +
            "            \"PBCR0D0055\": 1,\n" +
            "            \"PBCR0D0056\": 1,\n" +
            "            \"PBCR0D0020\": 1,\n" +
            "            \"PBCR0D0021\": 1,\n" +
            "            \"PBCR0D0022\": 1,\n" +
            "            \"PBCR0D0023\": 1,\n" +
            "            \"PBCR0D0057\": 1,\n" +
            "            \"PBCR0D0058\": 1,\n" +
            "            \"PBCR0D0059\": 1,\n" +
            "            \"PBCR0D0060\": 1,\n" +
            "            \"PBCR0D0024\": 1,\n" +
            "            \"PBCR0D0025\": 1,\n" +
            "            \"PBCR0D0026\": 1,\n" +
            "            \"PBCR0D0027\": 1,\n" +
            "            \"PBCR0D0061\": 1,\n" +
            "            \"PBCR0D0062\": 1,\n" +
            "            \"PBCR0D0063\": 1,\n" +
            "            \"PBCR0D0064\": 1,\n" +
            "            \"PBCR0D0028\": 1,\n" +
            "            \"PBCR0D0029\": 1,\n" +
            "            \"PBCR0D0030\": 1,\n" +
            "            \"PBCR0D0031\": 1,\n" +
            "            \"PBCR0D0065\": 1,\n" +
            "            \"PBCR0D0032\": 1,\n" +
            "            \"PBCR0Z0003\": 1,\n" +
            "            \"PBCR0D0042\": 1,\n" +
            "            \"PBCR0D0006\": 1,\n" +
            "            \"PBCR0D0007\": 1,\n" +
            "            \"PBCR0D0049\": 1,\n" +
            "            \"PBCR0D0016\": 1,\n" +
            "            \"PBCR0D0017\": 1,\n" +
            "            \"PBCR0D0043\": 2.21,\n" +
            "            \"PBCR0D0008\": 2.21,\n" +
            "            \"PBCR0D0009\": 2.21,\n" +
            "            \"PBCR0D0044\": 2.21,\n" +
            "            \"PBCR0D0010\": 2.21,\n" +
            "            \"PBCR0D0011\": 2.21,\n" +
            "            \"PBCR0D0051\": 2.21,\n" +
            "            \"PBCR0D0033\": 2.21,\n" +
            "            \"PBCR0D0034\": 2.21,\n" +
            "            \"PBCR0Z0011\": 2.21,\n" +
            "            \"PBCR0Z0004\": 2.21,\n" +
            "            \"PBCR0Z0005\": 2.21,\n" +
            "            \"PBCR0Z0006\": 2.21,\n" +
            "            \"PBCR0D0066\": 1,\n" +
            "            \"PBCR0D0045\": 1,\n" +
            "            \"PBCR0D0012\": 1,\n" +
            "            \"PBCR0Z0008\": 1,\n" +
            "            \"PBCR0D0050\": 1,\n" +
            "            \"PBCR0D0018\": 1,\n" +
            "            \"PBCR0Z0009\": 1,\n" +
            "            \"PBCR0D0067\": 2.21,\n" +
            "            \"PBCR0D0068\": 2.21,\n" +
            "            \"PBCR0E0007\": 1,\n" +
            "            \"PBCR0E0008\": 1,\n" +
            "            \"PBCR0E0009\": 1,\n" +
            "            \"PBCR0E0010\": 1,\n" +
            "            \"PBCR0E0011\": 1,\n" +
            "            \"PBCR0E0012\": 1,\n" +
            "            \"PBCR0E0013\": 1,\n" +
            "            \"PBCR0E0014\": 1,\n" +
            "            \"PBCR0E0015\": 1,\n" +
            "            \"PBCR0D0046\": 1,\n" +
            "            \"PBCR0D0047\": 1,\n" +
            "            \"PBCR0D0048\": 1,\n" +
            "            \"PBCR0D0013\": 1,\n" +
            "            \"PBCR0D0014\": 1,\n" +
            "            \"PBCR0D0015\": 1,\n" +
            "            \"PBCR0D0040\": 1,\n" +
            "            \"PBCR0D0069\": 1,\n" +
            "            \"PBCR0D0071\": 1,\n" +
            "            \"PBCR0D0003\": 1,\n" +
            "            \"PBCR0D0035\": 2.21,\n" +
            "            \"PBCR0D0039\": 1,\n" +
            "            \"PBCR0D0036\": 1,\n" +
            "            \"PBCR0D0037\": 1,\n" +
            "            \"PBCR0D0072\": 1,\n" +
            "            \"PBCR0D0001\": 1,\n" +
            "            \"PBCR0B0006\": 1,\n" +
            "            \"PBCR0B0007\": 1,\n" +
            "            \"PBCR0B0005\": 1,\n" +
            "            \"PBCR0E0001\": 1,\n" +
            "            \"PBCR0B0003\": 1,\n" +
            "            \"PBCR0B0001\": 1,\n" +
            "            \"PBCR0B0002\": 1,\n" +
            "            \"PBCR0B0004\": 1,\n" +
            "            \"PBCR0E0002\": 1,\n" +
            "            \"PBCR0E0003\": 2.21,\n" +
            "            \"PBCR0E0004\": \"abcd\",\n" +
            "            \"PBCR0C0003\": 1,\n" +
            "            \"PBCR0D0070\": 2.21,\n" +
            "            \"PBCR0D0038\": 2.21,\n" +
            "            \"PBCR0D0002\": 1,\n" +
            "            \"PBCR0C0002\": 2.21,\n" +
            "            \"PBCR0E0005\": 1,\n" +
            "            \"PBCR0E0006\": 1,\n" +
            "            \"PBCR0Z0010\": 1,\n" +
            "            \"PBCR0E0016\": \"abcd\",\n" +
            "            \"PBCR0D0073\": 1,\n" +
            "            \"PBCR0D0074\": 1,\n" +
            "            \"PBCR0D0075\": 1.23,\n" +
            "            \"PBCR0D0076\": 1.23,\n" +
            "            \"PBCR0D0077\": 1,\n" +
            "            \"PBCR0D0078\": 1.23,\n" +
            "            \"PBCR0D0079\": 1.23,\n" +
            "            \"PBCR0D0080\": 1.23,\n" +
            "            \"PBCR0D0081\": 1.23,\n" +
            "            \"PBCR0D0082\": 1.23,\n" +
            "            \"PBCR0D0083\": 1.23,\n" +
            "            \"PBCR0D0084\": 1,\n" +
            "            \"PBCR0D0085\": 1,\n" +
            "            \"PBCR0D0086\": 1,\n" +
            "            \"PBCR0D0087\": 1,\n" +
            "            \"PBCR0D0088\": 1,\n" +
            "            \"PBCR0D0089\": 1,\n" +
            "            \"PBCR0D0090\": 1,\n" +
            "            \"PBCR0D0091\": 1\n" +
            "        }\n" +
            "    }\n" +
            "}";
    public static void main(String[] args) throws IOException {
        //String groupName = "consumer-indexCal-group";
        String groupName = "group01";
        //String topic ="index-calculate-topic";
        String topic ="test-topic01";
        // set up the producer
        KafkaProducer<String, String> producer;
        Properties props = new Properties();
        //props.put("bootstrap.servers", "10.143.90.22:6667,10.143.90.23:6667");
        props.put("bootstrap.servers", "localhost:9092;localhost:9093;localhost:9094");
        props.put("retries", 0);
        // props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);

        try {
            for (int i = 1; i < 100; i++) {
                /*String msg=String.format("{" +
                        "    \"app_no\": \"%s\",\n" +
                        "    \"channel\":\"meiyifen\"\n" +"}", System.nanoTime());*/
                String msg=String.format(jsonStr, System.nanoTime());
                // send lots of messages
                producer.send(new ProducerRecord<String, String>(topic,msg));

                // every so often send to a different topic
                if (i % 10 == 0) {
                    producer.flush();
                    System.out.println("Send msg number " + i);
                }
            }
        } catch (Throwable throwable) {
            System.out.printf("%s", throwable.getStackTrace());
        } finally {
            producer.close();
            System.out.printf("producer.close()");

        }

    }
}
