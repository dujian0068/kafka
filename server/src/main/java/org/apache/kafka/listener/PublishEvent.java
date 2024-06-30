package org.apache.kafka.listener;

public enum PublishEvent {
    ;


    private String type;

    PublishEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
