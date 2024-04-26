package org.apache.kafka.clients.producer;

import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MyTest implements Runnable{
    private static String bootServer = "localhost:9092";
    private static String TOPIC_NAME = "TEST.TOPIC";

    private static String str = null;




    public static void main(String[] args) throws ExecutionException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        // 2000000
        for (int i = 0; i < 2000000; i++) {
            sb.append("helloKafka");
        }
        str = sb.toString();
        for (int i =0 ;i<5;i++){
            Thread thread = new Thread(new MyTest());
            thread.start();
        }
        Thread.sleep(100000);
        System.out.println("main exit");
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getId() + "thread start=========================");
        // 设置参数
        Properties props = new Properties();
        // 指定服务器配置【ip：端口】
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootServer);

        // key/value 序列化
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");

        //props.put(ProducerConfig.LINGER_MS_CONFIG, "10000000"); // 例如，设置为1000毫秒
        //props.put("max.block.ms", 600000 );
        props.put("max.request.size", 26214400);
        // props.put(ProducerConfig.BUFFER_MEMORY_CONFIG , 320 * 1024 * 1024);
        //props.put("batch.size", 2048);

        // 创建生产消息的客户端，传入参数
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        long allTime = 0L;
        long appendTime = 0L;

        for (int i = 0; i < 5000; i++) {
            // 创建消息；key：作用是决定了往哪个分区上发，value：具体要发送的消息内容
            String uuid = UUID.randomUUID().toString();
            ProducerRecord<String, String> message = new ProducerRecord<>(
                    TOPIC_NAME, 0, uuid, str);

            // 同步发送消息
            try {
                long start = System.currentTimeMillis();
                producer.send(message).get();


                allTime += (System.currentTimeMillis() - start);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(Thread.currentThread().getId() + "allTime："+ allTime/500);
        System.out.println(Thread.currentThread().getId() + "senderTime："+ appendTime/500);

    }
}
