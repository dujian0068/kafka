package org.apache.kafka.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class KafkaRunningEventPublisherListener implements KafkaEventPublisherListener{

    private List<KafkaRunningListener> listenerList;

    private String config;

    private static volatile KafkaEventPublisherListener KAFKA_EVENT_PUBLISHER_LISTENER = null;

    public static KafkaEventPublisherListener getInstance(String config){
        if(null == KAFKA_EVENT_PUBLISHER_LISTENER) {
            synchronized (KafkaRunningEventPublisherListener.class){
                if(null == KAFKA_EVENT_PUBLISHER_LISTENER){
                    KAFKA_EVENT_PUBLISHER_LISTENER = new KafkaRunningEventPublisherListener(config);
                }
            }
        }
        return KAFKA_EVENT_PUBLISHER_LISTENER;
    }

    private KafkaRunningEventPublisherListener(String config){
        this.config = config;
        listenerList = new ArrayList<>();
        this.loadListener();
    }


    @Override
    public void publishEvent(PublishEvent event) {
        switch (event.getType()){
            case "STARTING" :
                listenerList.forEach(listener->listener.starting(event));
            case "RUNNING":
                listenerList.forEach(listener -> listener.running(event) );
            case "STOPPING":
                listenerList.forEach(listener -> listener.stopping(event));
            default:
                // todo log.error
        }
    }

    @Override
    public void loadListener() {
        ServiceLoader<KafkaRunningListener> load = ServiceLoader.load(KafkaRunningListener.class, this.getClass().getClassLoader());
        for (KafkaRunningListener listener : load) {
            listener.init(config);
            listenerList.add(listener);
        }
    }
}
