package com.sai.mechat.models;

public class NotificationModel {
    private String id,message,notificationType,senderId;

    public NotificationModel() {
    }

    public NotificationModel(String id, String message, String notificationType, String senderId) {
        this.id = id;
        this.message = message;
        this.notificationType = notificationType;
        this.senderId = senderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
