package com.sau.chatapp.sauchatapp.entities;

import java.time.LocalDateTime;

public class MessageDto {
    private String sender;
    private String receiver;
    private String message;
    private LocalDateTime timestamp;

    // constructors, getters, and setters

    public MessageDto() {}

    public MessageDto(String sender, String receiver, String message, LocalDateTime timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
