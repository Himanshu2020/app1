package com.musipo.fcm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.musipo.constant.Config;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.musipo.dao.impl.ChatRoomDao;
import com.musipo.dao.impl.MessageDao;
import com.musipo.helper.MyApplication;
import com.musipo.logic.ProcessMsgNotiifcation;
import com.musipo.model.Message;
import com.musipo.model.User;
import com.musipo.ui.activity.ChatRoomActivity;
import com.musipo.ui.activity.MainActivity;
import com.musipo.util.DatabaseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import static android.R.attr.data;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    private Context _context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        _context = getApplicationContext();
        Log.e(TAG, "From: " + remoteMessage.getFrom());


        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                //JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(remoteMessage);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }


    private void handleDataMessage(RemoteMessage remoteMessage) {
        Log.e(TAG, "REMOTEMESSAGE::" + remoteMessage.toString());


        Map<String, String> fcmData = remoteMessage.getData();

        String title = fcmData.get("title");
        String message = fcmData.get("message");
        //String isBackground = fcmData.get("is_background");
        String payload = fcmData.get("payload");
        String timestamp = fcmData.get("timestamp");
        String flag = fcmData.get("FLAG");
        String imageUrl = "";

        Log.e(TAG, "title: " + title);
        Log.e(TAG, "message: " + message);
        boolean isBackground = false;
        Log.e(TAG, "isBackground: " + isBackground);
        Log.e(TAG, "payload: " + payload.toString());
        Log.e(TAG, "timestamp: " + timestamp);


        switch (Integer.parseInt(flag)) {
            case Config.PUSH_TYPE_CHATROOM:
                // push notification belongs to a chat room
                processChatRoomPush(title, isBackground, payload.toString());
                break;
            case Config.PUSH_TYPE_USER:
                // push notification is specific to user
                processUserMessage(title, isBackground, payload.toString());
                break;
        }


           /* if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }*/
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {

     /*   try {
            message = URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/


        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
/*        try {
            message = URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    /**
     * @auther Himanshu
     */
    private void handleNotificationMessage(JSONObject json) throws JSONException {


        JSONObject data = json.getJSONObject("data");

        String title = data.getString("title");
        String message = data.getString("message");
        boolean isBackground = data.getBoolean("is_background");
        String imageUrl = data.getString("image");
        String timestamp = data.getString("timestamp");
        JSONObject payload = data.getJSONObject("payload");
        String flag = data.getString("flag");

        Log.d(TAG, "title: " + title);
        Log.d(TAG, "isBackground: " + isBackground);
        Log.d(TAG, "flag: " + flag);
        Log.d(TAG, "data: " + data);

        if (flag == null)
            return;

        if (MyApplication.getInstance().getPrefManager().getUser() == null) {
            // user is not logged in, skipping push notification
            Log.e(TAG, "user is not logged in, skipping push notification");
            return;
        }

      /*  if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }*/

        switch (Integer.parseInt(flag)) {
            case Config.PUSH_TYPE_CHATROOM:
                // push notification belongs to a chat room
                processChatRoomPush(title, isBackground, payload.toString());
                break;
            case Config.PUSH_TYPE_USER:
                // push notification is specific to user
                processUserMessage(title, isBackground, payload.toString());
                break;
        }
    }


    /**
     * Processing chat room push message
     * this message will be broadcasts to all the activities registered
     */
    private void processChatRoomPush(String title, boolean isBackground, String data) {
        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);

                String chatRoomId = datObj.getString("chat_room_id");

                JSONObject mObj = datObj.getJSONObject("message");
                Message message = new Message();
                message.setMessage(mObj.getString("message"));
                message.setId(mObj.getString("message_id"));
                message.setCreatedDate(mObj.getString("created_at"));

                JSONObject uObj = datObj.getJSONObject("user");

                // skip the message if the message belongs to same user as
                // the user would be having the same message when he was sending
                // but it might differs in your scenario
                if (uObj.getString("user_id").equals(MyApplication.getInstance().getPrefManager().getUser().getId())) {
                    Log.e(TAG, "Skipping the push message as it belongs to same user");
                    return;
                }

                User user = new User();
                user.setId(uObj.getString("user_id"));
                user.setEmail(uObj.getString("email"));
                user.setName(uObj.getString("name"));
                message.setUser(user);

                // verifying whether the app is in background or foreground
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("type", Config.PUSH_TYPE_CHATROOM);
                    pushNotification.putExtra("message", message);
                    pushNotification.putExtra("chat_room_id", chatRoomId);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils(_context);
                    notificationUtils.playNotificationSound();
                } else {

                    // app is in background. show the message in notification try
                    Intent resultIntent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                    resultIntent.putExtra("chat_room_id", chatRoomId);
                    showNotificationMessage(getApplicationContext(), title, user.getName() + " : " + message.getMessage(), message.getCreatedDate(), resultIntent);
                }

            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }


    /**
     * Processing user specific push message
     * It will be displayed with / without image in push notification tray
     */
    private void processUserMessage(String title, boolean isBackground, String data) {

        DatabaseUtils.initializeDatabase(getApplicationContext(), true);

        ProcessMsgNotiifcation processMsgNotiifcation = new ProcessMsgNotiifcation();
        processMsgNotiifcation.msgNotificationProcess(data);
        Message message = processMsgNotiifcation.getMessageObjFromNotification(data);

        Log.e(TAG, "AAAAAAAA");
        // Message message =null;
        if (!isBackground) {
            Log.e(TAG, "BBBBB");
            //   JSONObject mObj = new JSONObject(data);
            String imageUrl = "";

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Log.e(TAG, "NotificationUtils");
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("type", Config.PUSH_TYPE_USER);
                pushNotification.putExtra("chat_room_id", message.getChatRoomId());
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(_context);
                notificationUtils.playNotificationSound();
            } else {

                MessageDao messageDao = new MessageDao();
                messageDao.save(message);
                Log.e(TAG, "MessageDao"+message.getUser().getName());

                Log.e(TAG, "message.getMessage()"+message.getMessage());

                Log.e(TAG, "MessageDao"+message.getCreatedAt()
                );

                Log.e(TAG, "MessageDao");
                // app is in background. show the message in notification try
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);


                showNotificationMessage(getApplicationContext(), message.getUser().getName(), message.getMessage(), message.getCreatedAt(), resultIntent);                // check for push notification image attachment
                /*if (TextUtils.isEmpty(imageUrl)) {
               //     showNotificationMessage(getApplicationContext(), message.getUser().getName(), message.getMessage(), message.getCreatedDate(), resultIntent);


                 //   showNotificationMessage(getApplicationContext(), "dddddddd", "DDDDDDDD", message.getCreatedDate(), resultIntent);
                } else {
                    // push notification contains image
                    // show it with the image
                    showNotificationMessageWithBigImage(getApplicationContext(), message.getUser().getName(), message.getMessage(), message.getCreatedDate(), resultIntent, imageUrl);
                }*/
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
            Log.e(TAG, "CCCCCCCCCCCCCc");
        }
    }


}
