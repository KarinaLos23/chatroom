package com.chatroomui.applicationui.dto;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Message {
    private Long id;

    private String message;
    private String sender;
    private ZonedDateTime timestamp;
    private String channelName;

    public Message() {
    }

    public Message(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(id, message1.id) && Objects.equals(message, message1.message) && Objects.equals(sender, message1.sender) && Objects.equals(timestamp, message1.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, sender, timestamp);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", user='" + sender + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
