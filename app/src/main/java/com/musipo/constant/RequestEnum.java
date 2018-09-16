package com.musipo.constant;

public enum RequestEnum {

    verifymobilenumber("verify_mobile_number"),

    login("login"),

    updateFcmRegistrationId("updateFcmRegistrationId"),

    fetchchatrooms("fetch_chat_rooms"),

    sendchatrooms("send_chat_rooms"),

    updateLastSeen("update_last_seen"),

    updateProfilePic("update_profile_pic"),

    getLastSeen("get_last_seen"),

    getStatus("get_status"),

    getPlayingStatus("get_playing_status"),

    updateUserName("update_Name"),

    getUser("get_user"),

    createChatRoom("createChatRoom"),

    getCustomerStatisticsDetails("customer_get_statistics_details"),

    updatePlayingStatus("updatePlayingStatus"),

    updateStatus("update_status");


    private final String requestMethodName;

    private RequestEnum(String statusText) {
        this.requestMethodName = statusText;
    }

    public static RequestEnum fromString(String text) {
        if (text != null) {
            for (RequestEnum b : RequestEnum.values()) {
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
