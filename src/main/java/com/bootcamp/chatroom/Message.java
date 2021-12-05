package com.bootcamp.chatroom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    private String message;
    private String user;
    private ZonedDateTime timestamp;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(id, message1.id) && Objects.equals(message, message1.message) && Objects.equals(user, message1.user) && Objects.equals(timestamp, message1.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, user, timestamp);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", user='" + user + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
