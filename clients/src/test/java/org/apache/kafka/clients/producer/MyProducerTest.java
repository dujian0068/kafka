package org.apache.kafka.clients.producer;

import org.apache.kafka.common.protocol.types.Field;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

//@BenchmarkMode(Mode.AverageTime)
//@State(Scope.Thread)
//@Fork(1)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
//@Warmup(iterations = 3)
//@Measurement(iterations = 5)
public class MyProducerTest {

    public static void main(String[] args) throws RunnerException {
        MyProducerTest myProducerTest = new MyProducerTest();

        myProducerTest.send();
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
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024 * 1024);
        producer = new KafkaProducer<>(properties);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String key = UUID.randomUUID().toString();
        StringBuilder sb = new StringBuilder();
        for (int i =  0; i< 100; i++){
            sb.append(key);
        }

        record = new ProducerRecord<>(topic, key, sb.toString());
    }

    //@Benchmark
    public void send(){
        long[] time = new long[20];
        for (int j = 0;j<20; j++){
            long start = System.currentTimeMillis();
            for (int i = 0; i< 1000000; i++){
                producer.send(record);
            }
            long end = System.currentTimeMillis();
            time[j] = end - start;
        }
        System.out.println(Arrays.toString(time));
        System.out.println(average(time));
        System.out.println(standardDiviation(time));


    }

    /**
     * 传入一个数列x计算平均值
     * @param x
     * @return 平均值
     */
    public static double average(long[] x) {
        int n = x.length;            //数列元素个数
        double sum = 0;
        for (long i : x) {        //求和
            sum+=i;
        }
        return sum/n;
    }

    /**
     * 传入一个数列x计算方差
     * 方差s^2=[（x1-x）^2+（x2-x）^2+......（xn-x）^2]/（n）（x为平均数）
     * @param x 要计算的数列
     * @return 方差
     */
    public static double variance(long[] x) {
        int n = x.length;            //数列元素个数
        double avg = average(x);    //求平均值
        double var = 0;
        for (double i : x) {
            var += (i-avg)*(i-avg);    //（x1-x）^2+（x2-x）^2+......（xn-x）^2
        }
        return var/n;
    }

    /**
     * 传入一个数列x计算标准差
     * 标准差σ=sqrt(s^2)，即标准差=方差的平方根
     * @param x 要计算的数列
     * @return 标准差
     */
    public static double standardDiviation(long[] x) {
        return  Math.sqrt(variance(x));
    }
}
