package com.kafka.demo03.OldConsumerHighLevelAPI;

import java.io.UnsupportedEncodingException;


import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;


public class Consumerwork implements Runnable {
    @SuppressWarnings("rawtypes")
    private KafkaStream m_stream;
    private int m_threadNumber;
    @SuppressWarnings("rawtypes")
    public Consumerwork(KafkaStream a_stream,int a_threadNumber) {
        // TODO Auto-generated constructor stub
        m_threadNumber = a_threadNumber;
        m_stream = a_stream;
    }
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("Thread " + m_threadNumber + ": start." );
        ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
        while (it.hasNext())
            try {
                MessageAndMetadata<byte[], byte[]> thisMetadata=it.next();
                String jsonStr = new String(thisMetadata.message(),"utf-8") ;
                System.out.println("@@@@@@@@@@@@@@Thread " + m_threadNumber + ": " +jsonStr);
                System.out.println("@@@@@@@@@@@@@@partion"+thisMetadata.partition()+",offset:"+thisMetadata.offset());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
}