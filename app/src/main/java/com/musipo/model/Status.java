package com.musipo.model;

import java.io.Serializable;

/**
 * Created by G510 on 04-06-2017.
 */

public class Status extends  CommanVO implements Serializable{

    private String id;
    private String statusMsg;
    private String userId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id='" + id + '\'' +
                ", statusMsg='" + statusMsg + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
