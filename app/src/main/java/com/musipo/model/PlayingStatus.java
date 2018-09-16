package com.musipo.model;

import java.io.Serializable;

/**
 * Created by G510 on 01-07-2017.
 */

public class PlayingStatus extends CommanVO implements Serializable {

    private String id;
    private String status;
    private String playingSrc;
    private String playingInfo;
    private String userId;
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlayingSrc() {
        return playingSrc;
    }

    public void setPlayingSrc(String playingSrc) {
        this.playingSrc = playingSrc;
    }

    public String getPlayingInfo() {
        return playingInfo;
    }

    public void setPlayingInfo(String playingInfo) {
        this.playingInfo = playingInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PlayingStatus{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", playingSrc='" + playingSrc + '\'' +
                ", playingInfo='" + playingInfo + '\'' +
                '}';
    }
}
