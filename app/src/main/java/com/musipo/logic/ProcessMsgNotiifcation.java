package com.musipo.logic;

import android.util.Log;

import com.google.gson.Gson;
import com.musipo.dao.impl.ChatRoomDao;
import com.musipo.model.ChatRoom;
import com.musipo.model.Message;
import com.musipo.model.User;
import com.musipo.restcall.JsonController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by G510 on 20-08-2017.
 */

public class ProcessMsgNotiifcation {

    private static final String TAG = ProcessMsgNotiifcation.class.getSimpleName();

    public ProcessMsgNotiifcation() {

    }

    final public User getReceiverUserFromNotification(String data) {
        User user = new User();
        JSONObject mObj = null;

        try {

            JSONObject uObj = mObj.getJSONObject("receiver_user");
            user.setId(uObj.getString("id"));
            user.setEmail(uObj.getString("email"));
            user.setName(uObj.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    final public User getSenderUserFromNotification(String data) {
        User user = new User();
        JSONObject mObj = null;

        try {

            JSONObject uObj = mObj.getJSONObject("sender_user");
            user.setId(uObj.getString("id"));
            user.setEmail(uObj.getString("email"));
            user.setName(uObj.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    final public Message getMessageObjFromNotification(String data) {

        Message message = new Message();
        try {
            JSONObject mObj = new JSONObject(data);
            JsonController<Message> jsonController = new JsonController<Message>();

            message = jsonController
                    .getModelFromData(data, Message.class);

            User user = new User();
            JsonController<User> jsonController1 = new JsonController<User>();

            user = jsonController1
                    .getModelFromData(mObj.getString("sender_user"), User.class);

           //
   //         Log.d(TAG,"sender_user.. Notification"+user);
        //    Log.d(TAG,"Messge.. Notification"+message);

            message.setUser(user);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"sender_user.. Notification");

        }

        return message;
    }

    public void msgNotificationProcess(String data) {

        try {
            JSONObject mObj = new JSONObject(data);
            Message message = new Message();
            String chatRoomID = mObj.getString("chat_room_id");


            JsonController<Message> jsonController = new JsonController<Message>();

              message = jsonController
                    .getModelFromData(data, Message.class);

            User user = new User();
            String uObj = mObj.getString("sender_user");
            JsonController<User> jsonController1 = new JsonController<User>();
            user = jsonController1
                    .getModelFromData(uObj, User.class);

           // Log.d(TAG,"USER  "+user);

            message.setUser(user);

       //     Log.d(TAG,"MESSAGE  "+message);

            ChatRoomDao chatRoomDao = new ChatRoomDao();
            ChatRoom chatRoom = chatRoomDao.find(chatRoomID);

            if (chatRoom == null) {
                chatRoom = new ChatRoom();
                chatRoom.setId(chatRoomID);
                chatRoom.setCreatedDate(mObj.getString("created_at"));
                chatRoom.setUserId(user.getId());
                chatRoom.setName(user.getName());
                chatRoom.setAssociatedUserId(mObj.getString("associated_user_id"));
                chatRoomDao.save(chatRoom);
            }

            chatRoomDao.updateMesssagAndCount(chatRoomID, message.getMessage());

        } catch (JSONException e) {
            e.printStackTrace();

            Log.d(TAG,"MJSONExceptionESSAGE  ");
        }


    }

}
