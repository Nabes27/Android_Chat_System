package com.example.chat_app2;

import com.google.firebase.Timestamp;

public class Message {
    private String id;
    private String content;
    private String senderName;
    private Timestamp timestamp;

    public Message() {
        // Required empty constructor for Firebase
    }

    public Message(String content, String senderName, Timestamp timestamp) {
        this.content = content;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }

    // Getters
    public String getId() { return id; }
    public String getContent() { return content; }
    public String getSenderName() { return senderName; }
    public Timestamp getTimestamp() { return timestamp; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setContent(String content) { this.content = content; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}