package com.musipo.notification;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.musipo.constant.Constant;
import com.musipo.constant.Params;
import com.musipo.model.Message;
import com.musipo.model.User;
import com.musipo.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Himanshu Bramhvanshi on 09-09-2017.
 */

public class MessageNotification {

    // Send message notification

    final public void sendMsgNotification(Message message, User senderUser, User receiverUser) {

        JSONObject jsonPayload = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(
                        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().serializeNulls().create();

        try {

            jsonPayload.put("chat_room_id", message.getChatRoomId());
            jsonPayload.put("message", message.getMessage());
            jsonPayload.put("sync_id", message.getSyncId());
            jsonPayload.put("message_id", message.getId());
            jsonPayload.put("created_at", Utils.getFromateDate(new Date()));
            jsonPayload.put("sender_user", gson.toJson(senderUser));
            jsonPayload.put("receiver_user", gson.toJson(receiverUser));

            Log.i("jsonPayload", "jsonPayload" + jsonPayload);
            jsonObject.put("title", "sendMessage");
            jsonObject.put("message", "Message sent");
            jsonObject.put("isBackground", "true");
            jsonObject.put("payload", jsonPayload);
            jsonObject.put("image", null);
            jsonObject.put("timestamp", "sendMessage");
            jsonObject.put(Params.FLAG.toString(), Constant.PUSH_TYPE_USER);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            PushNotification.pushFCMNotification(receiverUser.getFcmId(), jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
