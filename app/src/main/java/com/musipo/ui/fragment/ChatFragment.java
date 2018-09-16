package com.musipo.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musipo.R;
import com.musipo.adapter.ChatRoomsAdapter;
import com.musipo.constant.Config;
import com.musipo.constant.RequestEnum;
import com.musipo.dao.impl.ChatRoomDao;
import com.musipo.dao.impl.UserDao;
import com.musipo.helper.MyApplication;
import com.musipo.helper.SimpleDividerItemDecoration;
import com.musipo.model.ChatRoom;
import com.musipo.model.Message;
import com.musipo.model.User;
import com.musipo.model.json.ResponceJson;
import com.musipo.restcall.IServiceListener;
import com.musipo.restcall.JsonController;
import com.musipo.restcall.Request;
import com.musipo.restcall.RequestMethod;
import com.musipo.restcall.ServiceFrontController;
import com.musipo.service.ServerSyncServices;
import com.musipo.ui.activity.ChatRoomActivity;
import com.musipo.ui.activity.UserContactListActivity;
import com.musipo.util.DatabaseUtils;
import com.musipo.util.UiUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    private String TAG = ChatFragment.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<ChatRoom> chatRoomArrayList;
    private ChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;
    private View rootView;
    private Context _context;
    private User LoggedUser = MyApplication.getInstance().getPrefManager().getUser();
    private UserDao userDao;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat, container,
                false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
         DatabaseUtils.initializeDatabase(getContext(), false);
        init();
        addChatRooms();


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), UserContactListActivity.class);
                startActivity(in);
            }
        });


        return rootView;
    }

    /*public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // checking for type intent filter


            Log.e(TAG, "GCM registration id is sDDDDDDDDDDDDDDDDDDDDDDDDDDent to our server");

            if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                // gcm successfully registered
                // now subscribe to `global` topic to receive app wide notifications

            } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                // gcm registration id is stored in our server's MySQL
                Log.e(TAG, "GCM registration id is sent to our server");

            } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                // new push notification is received
                handlePushNotification(intent);
            }
        }
    };*/


    private void init() {
        _context = getContext();


    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // checking for type intent filter


            Log.e(TAG, "GCM registration id is sDDDDDDDDDDDDDDDDDDDDDDDDDDent to our server");

            if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                // gcm successfully registered
                // now subscribe to `global` topic to receive app wide notifications

            } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                // gcm registration id is stored in our server's MySQL
                Log.e(TAG, "GCM registration id is sent to our server");

            } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                // new push notification is received
                handlePushNotification(intent);
            }
        }
    };

    /* @Override
     public void onCreate(Bundle savedInstanceState) {

         super.onCreate(savedInstanceState);
          mReceiver = new ChatBroadCastReceiver();
         LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver,new IntentFilter("chatupdater"));
         Log.i("chat", "I am in onCreate");

     }
     public BroadcastReceiver mReceiver = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             Log.i("chat","I am in BroadCastReceiver");
             String msg = intent.getStringExtra("msg");
             Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
         }
     };
 */
    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        String chatRoomId = null;
        Message message = null;
        if (type == Config.PUSH_TYPE_CHATROOM) {
            message = (Message) intent.getSerializableExtra("message");
            chatRoomId = intent.getStringExtra("chat_room_id");

            if (message != null && chatRoomId != null) {
                updateRow(chatRoomId, message);
            }
        } else if (type == Config.PUSH_TYPE_USER) {
            // push belongs to user alone
            // just showing the message in a toast
            message = (Message) intent.getSerializableExtra("message");
            //Toast.makeText(getContext(), "New push: " + message.getMessage(), Toast.LENGTH_LONG).show();
            if (message != null && chatRoomId != null) {
                updateRow(chatRoomId, message);
            }
        }


    }

    private void updateRow(String chatRoomId, Message message) {
        for (ChatRoom cr : chatRoomArrayList) {
            if (cr.getId().equals(chatRoomId)) {
                int index = chatRoomArrayList.indexOf(cr);
                cr.setLastMessage(message.getMessage());
                cr.setUnreadCount(cr.getUnreadCount() + 1);
                chatRoomArrayList.remove(index);
                chatRoomArrayList.add(index, cr);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void fetchChatRooms() {
        chatRoomArrayList = new ArrayList<>();

        List<ChatRoom>  newDataList = new ArrayList<>();
        ChatRoomDao chatRoomDao = new ChatRoomDao();

        List<ChatRoom> dataList = chatRoomDao.findAll();

        User user=null;
        userDao = new UserDao();

       /* for (ChatRoom chatRoom: dataList) {
            //userDao.find()
          //  String associatedUserId = chatRoom.getAssociatedUserId().trim();
            String chatUserId = chatRoom.getUserId().trim();

          *//*  if (!LoggedUser.getId().equals(associatedUserId)) {
               user = userDao.find(associatedUserId);

            }else if (!LoggedUser.getId().equals(chatUserId)) {
                Log.e(TAG, "userDao" + userDao);
                user = userDao.find(chatUserId);
                Log.e(TAG, "Char" + user);

            }*//*

        //    chatRoom.setName(user.getName());
            newDataList.add(chatRoom);
        }*/

        if (dataList != null) {
            chatRoomArrayList.addAll(dataList);
        }

    }

    private void addChatRooms() {
        fetchChatRooms();
        mAdapter = new ChatRoomsAdapter(_context.getApplicationContext(), chatRoomArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_context.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(_context.getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ChatRoomsAdapter.RecyclerTouchListener(_context.getApplicationContext(), recyclerView, new ChatRoomsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity

                ChatRoom chatRoom = chatRoomArrayList.get(position);
                Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                Log.e(TAG, "Char" + LoggedUser);

                String associatedUserId = chatRoom.getAssociatedUserId().trim();
                String chatUserId = chatRoom.getUserId().trim();

                if (LoggedUser.getId().equals(associatedUserId)) {


                    intent.putExtra("user_id", chatUserId);
                    // Log.e(TAG, "Test chatUserId Char user........");
                }
                if (LoggedUser.getId().equals(chatUserId)) {
                    intent.putExtra("user_id", associatedUserId);
                    //  Log.e(TAG, "Test associatedUserId Char user.........");
                }
                //   intent.putExtra("user_id", chatRoom.getAssociatedUserId());
                //  Log.e(TAG, "Char user data chatRoom.getAssociatedUserId()"+chatRoom.getAssociatedUserId());
                //    Log.e(TAG, "Char user data chatRoom.getUserId()"+chatRoom.getUserId());

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void fetchChatRooms12(Context context) throws Exception {
        // sending gcm token to server
        // Log.e(TAG, "sendRegistrationToServer: " + token);

        User user = MyApplication.getInstance().getPrefManager().getUser();

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.fetchchatrooms.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "chat/findUserChatrooms");

        pramas.put("userID", user.getId());

        ServiceFrontController.getInstance().fireCommand(context,
                RequestEnum.fetchchatrooms.toString(), pramas, new IServiceListener() {
                    @Override
                    public void result(String jsonResult) {

                        ResponceJson responceObj = null;
                        responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);
                        if (responceObj.isStatus()) {
                            Gson gson = new Gson();
                          /*  gson = new GsonBuilder()
                                    .disableHtmlEscaping()
                                    .setFieldNamingPolicy(
                                            FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                    .setPrettyPrinting().serializeNulls().create();*/
                            Collection<ChatRoom> chatRoomList = null;

                            chatRoomList = (ArrayList<ChatRoom>) gson.fromJson(responceObj.getDataString(),
                                    new TypeToken<ArrayList<ChatRoom>>() {
                                    }.getType());


                            chatRoomArrayList.addAll(chatRoomList);
                            mAdapter.notifyDataSetChanged();

                            if (chatRoomArrayList != null) {

                                for (ChatRoom cr : chatRoomArrayList) {
                                    ServerSyncServices serverSyncServices = new ServerSyncServices(_context);
                                    serverSyncServices.getUser(cr.getUserId());
                                }
                                //getUser(cr.getUserId());}
                            }

                            Log.i("chatRoomList", "jsonString :" + chatRoomList);
                        }
                    }

                    @Override
                    public void fault(String message) {

                    }
                }, RequestMethod.GET);

    }

}
