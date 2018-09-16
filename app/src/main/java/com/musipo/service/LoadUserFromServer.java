package com.musipo.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musipo.constant.RequestEnum;
import com.musipo.dao.impl.ChatRoomDao;
import com.musipo.dao.impl.PlayStatusDAO;
import com.musipo.dao.impl.StatusDAO;
import com.musipo.dao.impl.UserDao;
import com.musipo.helper.MyApplication;
import com.musipo.model.ChatRoom;
import com.musipo.model.PlayingStatus;
import com.musipo.model.Status;
import com.musipo.model.User;
import com.musipo.model.json.ResponceJson;
import com.musipo.restcall.IServiceListener;
import com.musipo.restcall.JsonController;
import com.musipo.restcall.Request;
import com.musipo.restcall.RequestMethod;
import com.musipo.restcall.ServiceFrontController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by G510 on 18-06-2017.
 */

public class LoadUserFromServer {

    private Context _context;

    public LoadUserFromServer(Context _context) {
        this._context = _context;
    }


    public void syncUser() {
        User user = MyApplication.getInstance().getPrefManager().getUser();

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.getUser.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "user/getUserList");


        if(user!=null)
        pramas.put("userID", user.getId());

        try {
            ServiceFrontController.getInstance().fireCommand(_context,
                    RequestEnum.getUser.toString(), pramas, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);

                            if (responceObj.isStatus()) {
                                Gson gson = new Gson();

                                ArrayList<User> userList = null;

                                userList = (ArrayList<User>) gson.fromJson(responceObj.getDataString(),
                                        new TypeToken<ArrayList<User>>() {
                                        }.getType());

                                Log.d("TT............",userList.toString());

                                UserDao userDao =  new UserDao();
                                userDao.saveList(userList);

                            }


                        }

                        @Override
                        public void fault(String message) {

                        }
                    }, RequestMethod.GET);
        } catch (Exception e) {

        }
    }

    public void fetchChatRooms(Context context) throws Exception {
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
                            ArrayList<ChatRoom> chatRoomList = null;

                            chatRoomList = (ArrayList<ChatRoom>) gson.fromJson(responceObj.getDataString(),
                                    new TypeToken<ArrayList<ChatRoom>>() {
                                    }.getType());

                            ChatRoomDao chatRoomDao =  new ChatRoomDao();
                            chatRoomDao.saveList(chatRoomList);


                            if (chatRoomList != null) {

                                for (ChatRoom cr : chatRoomList) {
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



    public void getPlayingStatus(User user)  {

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.getStatus.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "status/getPlayingStatus");

        pramas.put("userID", user.getId());

        try {
            ServiceFrontController.getInstance().fireCommand(_context,
                    RequestEnum.getStatus.toString(), pramas, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);

                            if(responceObj.isStatus()) {

                                Gson gson = new Gson();
                                PlayingStatus status = new PlayingStatus();

                                status = (PlayingStatus) gson.fromJson(responceObj.getDataString(),
                                        new TypeToken<PlayingStatus>() {
                                        }.getType());

                                PlayStatusDAO statusDAO =  new PlayStatusDAO();
                                statusDAO.save(status);

                            }
                        }

                        @Override
                        public void fault(String message) {

                        }
                    }, RequestMethod.GET);
        } catch (Exception e) {
        }

    }

}
