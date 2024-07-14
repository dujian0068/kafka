package org.apache.kafka.clients.admin;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Properties;

public class MyTopicTest {

    private static String SERVER_ADDRESS = "localhost:9092";
    private static String CLIENT_ID = "CreateKafkaTopic";
    private static String TOPIC_NAME = "JMH_TOPIC";
    int numPartitions = 1;
    short replicationFactor = 1;

    @Test
    public void createTopic(){
        Properties props = new Properties();
        props.put("bootstrap.servers", SERVER_ADDRESS); // Kafka集群的地址
        props.put("client.id", CLIENT_ID);
        AdminClient adminClient = AdminClient.create(props);
        NewTopic newTopic = new NewTopic(TOPIC_NAME, numPartitions, replicationFactor);

        adminClient.createTopics(Collections.singletonList(newTopic));
        adminClient.close();

    }


}
