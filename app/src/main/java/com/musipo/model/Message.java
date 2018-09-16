package com.musipo.model;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Message extends CommanVO implements Serializable {
    String id, message,chatRoomId,createdAt,syncId,msgStatus;
    User user = new User();

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSyncId() {
        return syncId;
    }

    public void setSyncId(String syncId) {
        this.syncId = syncId;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", chatRoomId='" + chatRoomId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", syncId='" + syncId + '\'' +
                ", msgStatus='" + msgStatus + '\'' +
                ", user=" + user +
                '}';
    }
}
