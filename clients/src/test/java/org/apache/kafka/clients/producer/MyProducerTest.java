package org.apache.kafka.clients.producer;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class MyProducerTest {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MyProducerTest.class.getSimpleName())
                .build();
        new Runner(opt).run();

    }
    static String bootstrapServers = "localhost:9092"; // 替换为你的Kafka服务器地址
    static String topic = "JMH_TOPIC"; // 替换为你的主题名称

    // Kafka Producer配置
    static Properties properties = new Properties();
    static  KafkaProducer<String, String> producer = null;
    static ProducerRecord<String, String> record = null;
    static {
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("key.serializer", org.apache.kafka.common.serialization.StringSerializer.class);
        properties.put("value.serializer", org.apache.kafka.common.serialization.StringSerializer.class);
        producer = new KafkaProducer<>(properties);
        String key = UUID.randomUUID().toString();
        record = new ProducerRecord<>(topic, key, key);
    }

    @Benchmark
    public void send(){

        for (int i = 0; i< 10000000; i++){
            producer.send(record);
        }
    }
}
