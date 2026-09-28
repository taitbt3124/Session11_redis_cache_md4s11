package com.example.session11.dto;

public class AlertRequest {

    private String topicListen;
    private String type;
    private String message;

    public AlertRequest() {
    }

    public String getTopicListen() {
        return topicListen;
    }

    public void setTopicListen(String topicListen) {
        this.topicListen = topicListen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
