package org.apache.kafka.listener;

public interface KafkaEventPublisherListener {

    void publishEvent(PublishEvent event);

    void loadListener();

}
