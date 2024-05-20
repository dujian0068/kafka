package org.apache.kafka.clients.producer;

import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MyTest {

    private static String topic = "TEST.TOPIC-1";
    private static String server = "localhost:9092";
    private static Properties props;

    static {
        props = new Properties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);

        //props.put(ProducerConfig.ACKS_CONFIG, "all");
        //props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");
        props.put(ProducerConfig.LINGER_MS_CONFIG, "10000000"); // 例如，设置为1000毫秒
        props.put("max.block.ms", 600000);
        props.put("max.request.size", 26214400);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Producer<String, String> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 1; i++) {
            ProducerRecord<String, String> message = new ProducerRecord<>(topic, "mssage");
            Future<RecordMetadata> send = producer.send(message);
            RecordMetadata recordMetadata = send.get();
            System.out.println(recordMetadata.toString());
        }
        producer.close();
        System.out.println();
    }
}
