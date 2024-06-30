package org.apache.kafka.listener;

public class SendKafkaMessageKafkaRunningListener implements KafkaRunningListener{
    @Override
    public void init(String config) {
        return;
    }

    @Override
    public void starting(PublishEvent event) {
        return;
    }

    @Override
    public void running(PublishEvent event) {
        return;
    }

    @Override
    public void stopping(PublishEvent event) {
        return;
    }
}
