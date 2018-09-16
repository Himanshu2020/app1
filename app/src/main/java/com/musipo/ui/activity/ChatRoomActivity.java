package com.musipo.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musipo.R;
import com.musipo.adapter.ChatRoomThreadAdapter;
import com.musipo.constant.Config;
import com.musipo.constant.EndPoints;
import com.musipo.constant.RequestEnum;
import com.musipo.constant.StatusEnum;
import com.musipo.dao.impl.ChatRoomDao;
import com.musipo.dao.impl.MessageDao;
import com.musipo.dao.impl.UserDao;
import com.musipo.helper.MyApplication;
import com.musipo.fcm.NotificationUtils;
import com.musipo.model.ChatRoom;
import com.musipo.model.Message;
import com.musipo.model.User;
import com.musipo.model.json.ResponceJson;
import com.musipo.notification.MessageNotification;
import com.musipo.restcall.IServiceListener;
import com.musipo.restcall.JsonController;
import com.musipo.restcall.RequestMethod;
import com.musipo.restcall.ServiceFrontController;
import com.musipo.service.ServerSyncServices;
import com.musipo.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.protocol.HTTP;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static com.musipo.constant.RequestEnum.createChatRoom;


public class ChatRoomActivity extends AppCompatActivity {

    private String TAG = ChatRoomActivity.class.getSimpleName();

    private String chatRoomId;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<Message> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ImageView btnSend;
    private String chatUserId;
    private String title;
    private TextView lastSeenTv;
    private TextView chatTitleTV;
    EmojiconEditText inputMessage;
    MessageDao messageDao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chats);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openProfileActivity();
            }
        });

        messageDao = new MessageDao();
        lastSeenTv = (TextView) findViewById(R.id.action_bar_title_lastseen);
        chatTitleTV = (TextView) findViewById(R.id.action_bar_title);

        inputMessage = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);
        btnSend = (ImageView) findViewById(R.id.submit_btn);
        showEmoji();


        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chat_room_id");
        chatUserId = intent.getStringExtra("user_id");
        title = intent.getStringExtra("name");
        getSupportActionBar().setTitle(title);


        Log.e(TAG, "chatUserId........." + chatUserId);

        chatTitleTV.setText(title);

        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
   /*     getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/

        if (chatRoomId == null) {

            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        messageArrayList = new ArrayList<>();

        messageDao = new MessageDao();
        List<Message> dataList = messageDao.findAllMsgByChatRoomId(chatRoomId);

        if (dataList != null)
            messageArrayList.addAll(dataList);
        // self user id is to identify the message owner
        String selfUserId = MyApplication.getInstance().getPrefManager().getUser().getId();
        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList, selfUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                }
            }
        };

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //   sendMessageService(v.getContext());

                    if (chatRoomId == null || chatRoomId.isEmpty()) {
          /*  ServerSyncServices serverSyncServices = new ServerSyncServices();
            serverSyncServices.createChatRoom(title, chatUserId);*/
                        createChatRoom(title, chatUserId);
                    } else {


                        Message message = new Message();
                        message.setChatRoomId(chatRoomId);
                        message.setMessage(inputMessage.getText().toString());
                        message.setSyncId(Utils.md5("syncId") + Utils.getOtpNum(5));
                        message.setDeletedFlag(0);
                        message.setMsgStatus(StatusEnum.NOT_DELIVERED.toString());

                        // Dummy message id for Android to android communication . This is not server id.
                        message.setId(Utils.md5("messageId") + Utils.getOtpNum(5));
                        sendMsg(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // fetchChatThread()
        // fet;
        fetchChatThreadFrmDB();
        try {
            getLastSeen(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ChatRoomDao chatRoomDao = new ChatRoomDao();
        chatRoomDao.cleaMsgReadCount(chatRoomId);
        // Check chat room is available or not

    }

    private final void openProfileActivity() {
        Intent intent = new Intent(this, ChatProfileActivity.class);
        intent.putExtra("chat_room_id", "ddd");
        intent.putExtra("name", title);
        startActivity(intent);
    }

    private void showEmoji() {

        View rootView = findViewById(R.id.root_view);
        ImageView emojiButton = (ImageView) findViewById(R.id.emoji_btn);
        EmojIconActions emojIcon = new EmojIconActions(getApplicationContext(), rootView, inputMessage, emojiButton);
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.ShowEmojIcon();
        emojIcon.setUseSystemEmoji(false);
        inputMessage.setUseSystemDefault(false);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });
       /* EmojiconEditText emojiconEditText2 = (EmojiconEditText) findViewById(R.id.emojicon_edit_text2);
        emojIcon.addEmojiconEditTextList(emojiconEditText2);*/
    }


    @Override
    protected void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications(this);
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     */
    private void handlePushNotification(Intent intent) {


        Message message = (Message) intent.getSerializableExtra("message");

        String chatRoomId = intent.getStringExtra("chat_room_id");
        message.setChatRoomId(chatRoomId);

        Log.e(TAG, "chat_room_id: " + chatRoomId);
        Log.e(TAG, "messageS: " + message);

        if (message != null && chatRoomId != null) {
            messageArrayList.add(message);
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
        }
        messageDao.save(message);
    }

    /**
     * Posting a new message in chat room
     * will make an http call to our server. Our server again sends the message
     * to all the devices as push notification
     */

    final private void sendMsg(Message message) {

        User user = MyApplication.getInstance().getPrefManager().getUser();

        UserDao userDao = new UserDao();

        User senderUser = userDao.find(user.getId());
        User receiverUser = userDao.find(chatUserId);

     //   Log.d(TAG, "senderUser() Query : " + senderUser);
       // Log.d(TAG, "receiverUser() Query : " + receiverUser);

        message.setUser(user);
        MessageDao messageDao = new MessageDao();
        messageDao.save(message);

        MessageNotification messageNotification = new MessageNotification();
        messageNotification.sendMsgNotification(message, senderUser, receiverUser);

        inputMessage.setText("");

        messageArrayList.add(message);
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getItemCount() > 1) {
            // scrolling to bottom of the recycler view
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
        }

      /*  try {
            sendMessageService(this, message);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

       /*  messageDao = new MessageDao();
        messageDao.save(message);*/

    }

    public void sendMessageService(Context _context, Message messagetemp) throws Exception {
        // sending gcm token to server
        // Log.e(TAG, "sendRegistrationToServer: " + token);

        final String message = this.inputMessage.getText().toString().trim();
        inputMessage.setText("");


        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        //     User user = MyApplication.getInstance().getPrefManager().getUser();

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.sendchatrooms.toString(),
                com.musipo.restcall.Request.class);

        HashMap<String, String> params = new HashMap<>();
        params.put(com.musipo.restcall.Request.methodName, "message/sendMessage");

        String message1 = "";
        try {
            message1 = URLEncoder.encode(message,
                    HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        params.put("senderUserID", MyApplication.getInstance().getPrefManager().getUser().getId());
        params.put("message", message1);
        params.put("chatRoomID", chatRoomId);
        params.put("syncID", messagetemp.getSyncId());
        params.put("receiverUserID", chatUserId);


        Context context = null;
        ServiceFrontController.getInstance().fireCommand(context,
                RequestEnum.sendchatrooms.toString(), params, new IServiceListener() {
                    @Override
                    public void result(String jsonResult) {

                        ResponceJson responceObj = null;
                        responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);
                        if (responceObj.isStatus()) {

                            try {
                                JSONObject commentObj = new JSONObject(responceObj.getDataString());

                                String commentId = commentObj.getString("message_id");
                                String commentText = commentObj.getString("message");
                                String createdAt = commentObj.getString("created_at");
                                String syncID = commentObj.getString("sync_id");
                                String chatRoomId = commentObj.getString("chat_room_id");

                                try {
                                    commentText = URLDecoder.decode(
                                            commentText, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }


                                JSONObject userObj = commentObj.getJSONObject("user");
                                String userId = userObj.getString("id");
                                String userName = userObj.getString("name");

                                User user1 = new User(userId, "Sent", null);


                                Message message = new Message();
                                message.setId(commentId);
                                message.setMessage(commentText);
                                message.setCreatedAt(createdAt);
                                message.setUser(user1);
                                message.setSyncId(syncID);
                                message.setChatRoomId(chatRoomId);

                                MessageDao messageDao = new MessageDao();
                                messageDao.update(message);

                                messageArrayList.add(message);


                                mAdapter.notifyDataSetChanged();
                                if (mAdapter.getItemCount() > 1) {
                                    // scrolling to bottom of the recycler view
                                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), responceObj.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void fault(String message) {

                    }
                }, RequestMethod.POST);

    }


    public void createChatRoom(String name, String userId) {
        User user = MyApplication.getInstance().getPrefManager().getUser();

        ServiceFrontController.getInstance().addMethod(
                createChatRoom.toString(),
                com.musipo.restcall.Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(com.musipo.restcall.Request.methodName, "chat/createChatRoom");

        pramas.put("name", name);
        pramas.put("userID", userId);
        pramas.put("associatedUserId", user.getId());

        try {
            ServiceFrontController.getInstance().fireCommand(this,
                    createChatRoom.toString(), pramas, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);

                            if (responceObj.isStatus()) {
                                Gson gson = new Gson();
                                ChatRoom chatRoom;
                                chatRoom = gson.fromJson(responceObj.getDataString(),
                                        new TypeToken<ChatRoom>() {
                                        }.getType());


                                chatRoomId = chatRoom.getId();

                                Message message = new Message();
                                message.setChatRoomId(chatRoomId);
                                message.setMessage(inputMessage.getText().toString());
                                message.setSyncId(Utils.md5("syncId") + Utils.getOtpNum(5));
                                message.setDeletedFlag(0);
                                message.setMsgStatus(StatusEnum.NOT_DELIVERED.toString());

                                sendMsg(message);

                                ChatRoomDao chatRoomDao = new ChatRoomDao();
                                chatRoomDao.save(chatRoom);


                            }
                        }

                        @Override
                        public void fault(String message) {

                        }
                    }, RequestMethod.GET);
        } catch (Exception e) {
        }

    }

    /**
     * Fetching all the messages of a single chat room
     */


    private void fetchChatThreadFrmDB() {

        MessageDao messageDao = new MessageDao();
        List<Message> dataList = messageDao.findAll();

        if (dataList != null) {

            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
        }


    }

    private void fetchChatThread() {

        String endPoint = EndPoints.CHAT_THREAD.replace("_ID_", chatRoomId);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONArray commentsObj = obj.getJSONArray("messages");

                        for (int i = 0; i < commentsObj.length(); i++) {
                            JSONObject commentObj = (JSONObject) commentsObj.get(i);

                            String commentId = commentObj.getString("message_id");
                            String commentText = commentObj.getString("message");
                            String createdAt = commentObj.getString("created_at");

                            JSONObject userObj = commentObj.getJSONObject("user");
                            String userId = userObj.getString("user_id");
                            String userName = userObj.getString("username");
                            User user = new User(userId, userName, null);

                            Message message = new Message();
                            message.setId(commentId);
                            message.setMessage(commentText);
                            message.setCreatedAt(createdAt);
                            message.setUser(user);

                            messageArrayList.add(message);
                        }

                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    public void getLastSeen(Context _context) throws Exception {
        // sending gcm token to server
        // Log.e(TAG, "sendRegistrationToServer: " + token);


        ServiceFrontController.getInstance().addMethod(
                RequestEnum.getLastSeen.toString(),
                com.musipo.restcall.Request.class);

        HashMap<String, String> params = new HashMap<>();
        params.put(com.musipo.restcall.Request.methodName, "user/getlastseen");
        params.put("userID", chatUserId);

        Context context = null;
        ServiceFrontController.getInstance().fireCommand(context,
                RequestEnum.getLastSeen.toString(), params, new IServiceListener() {
                    @Override
                    public void result(String jsonResult) {

                        ResponceJson responceObj = null;
                        responceObj = JsonController.getRespnceJson(jsonResult);
                        if (responceObj.isStatus()) {
                            try {
                                String lastSeen = getTimeStamp(responceObj.getData().getString("seenAt"));
                                lastSeenTv.setText(lastSeen);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), responceObj.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void fault(String message) {

                    }
                }, RequestMethod.GET);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }


    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";
        Calendar calendar = Calendar.getInstance();
        String today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

}
