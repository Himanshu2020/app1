package com.musipo.model;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class ChatRoom extends CommanVO implements Serializable {
    String id, name, lastMessage, timestamp, userId,profilePic,associatedUserId;
    int unreadCount;

    public ChatRoom() {
    }

    public ChatRoom(String id, String name, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAssociatedUserId() {
        return associatedUserId;
    }

    public void setAssociatedUserId(String associatedUserId) {
        this.associatedUserId = associatedUserId;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", userId='" + userId + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", associatedUserId='" + associatedUserId + '\'' +
                ", unreadCount=" + unreadCount +
                '}';
    }
}
