package com.musipo.model;

import java.io.Serializable;

/**
 * Created by G510 on 18-06-2017.
 */

public class LastSeen extends CommanVO implements Serializable{
    private String id;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
