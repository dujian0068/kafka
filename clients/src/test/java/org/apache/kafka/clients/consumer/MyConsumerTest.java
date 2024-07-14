package org.apache.kafka.clients.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MyConsumerTest {
    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092"; // 替换为你的Kafka服务器地址
        String groupId = "your_group_id"; // 替换为你的消费者组ID
        String topic = "JMH_TOPIC"; // 替换为你的主题名称

        // Kafka Consumer配置
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("group.id", groupId);
        properties.put("key.deserializer", org.apache.kafka.common.serialization.StringDeserializer.class);
        properties.put("value.deserializer",org.apache.kafka.common.serialization.StringDeserializer.class);
        properties.put("auto.offset.reset", "earliest"); // 从最早的消息开始读取

        // 创建Kafka Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(topic));

        System.out.println("Connected to Kafka topic: " + topic);

        // 消费消息
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Received message: key = %s, value = %s, offset = %d, partition = %d%n",
                            record.key(), record.value(), record.offset(), record.partition());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
            System.out.println("Consumer closed.");
        }
    }
}
