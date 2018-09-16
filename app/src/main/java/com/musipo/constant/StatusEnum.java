package com.musipo.constant;

/**
 * Created by G510 on 11-08-2017.
 */

public enum StatusEnum {

    DELIVERED_AND_READ("DELIVERED_AND_READ"),
    NOT_DELIVERED("NOT_DELIVERED"),
    DELIVERED("DELIVERED");


    private final String requestMethodName;

    private StatusEnum(String statusText) {
        this.requestMethodName = statusText;
    }

    public static StatusEnum fromString(String text) {
        if (text != null) {
            for (StatusEnum b : StatusEnum.values()) {
                if (text.equalsIgnoreCase(b.requestMethodName)) {
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.requestMethodName;
    }
}
