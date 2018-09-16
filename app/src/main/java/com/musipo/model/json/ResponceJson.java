package com.musipo.model.json;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by G510 on 21-04-2017.
 */

public class ResponceJson implements Serializable {

    private boolean status;
    private JSONObject data;
    private String dataString;
    private String message;

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDataString() {
        return dataString;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponceJson{" +
                "status=" + status +
                ", data='" + data + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

