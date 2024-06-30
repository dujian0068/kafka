package org.apache.kafka.listener;

public interface KafkaRunningListener {

    void init(String config);

    void starting(PublishEvent event);

    void running(PublishEvent event);


    void stopping(PublishEvent event);
}
