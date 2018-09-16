package com.musipo.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musipo.constant.Config;
import com.musipo.constant.RequestEnum;
import com.musipo.dao.impl.ChatRoomDao;
import com.musipo.dao.impl.PlayStatusDAO;
import com.musipo.dao.impl.StatusDAO;
import com.musipo.fcm.MyFirebaseInstanceIDService;
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
import com.musipo.util.ImageUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import static com.musipo.helper.MyApplication.TAG;

/**
 * Created by G510 on 25-04-2017.
 */

public class ServerSyncServices {

    private Context _context;
    private Gson gson;

    public ServerSyncServices() {
    }

    public ServerSyncServices(Context _context) {
        this._context = _context;
    }

    public void createChatRoom(String name, String userId) {
        User user = MyApplication.getInstance().getPrefManager().getUser();

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.createChatRoom.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "chat/createChatRoom");

        pramas.put("name", name);
        pramas.put("userID", userId);
        pramas.put("associatedUserId", user.getId());

        try {
            ServiceFrontController.getInstance().fireCommand(_context,
                    RequestEnum.createChatRoom.toString(), pramas, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);

                            if (responceObj.isStatus()) {
                                gson = new Gson();
                                ChatRoom chatRoom;
                                chatRoom =  gson.fromJson(responceObj.getDataString(),
                                        new TypeToken<ChatRoom>() {
                                        }.getType());

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


    public void updateLastSeen() {
        User user = MyApplication.getInstance().getPrefManager().getUser();

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.updateLastSeen.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "user/updatelastseen");

        pramas.put("userID", user.getId());
        pramas.put("lastSeen", new Date().toString());

        try {
            ServiceFrontController.getInstance().fireCommand(_context,
                    RequestEnum.updateLastSeen.toString(), pramas, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);

                            if (responceObj.isStatus()) {

                            }
                        }

                        @Override
                        public void fault(String message) {

                        }
                    }, RequestMethod.GET);
        } catch (Exception e) {
        }

    }


    public void updateStatus() {
        User user = MyApplication.getInstance().getPrefManager().getUser();

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.updateStatus.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "status/updateStatus");

        pramas.put("userID", user.getId());
        pramas.put("message", "I am using music");

        try {
            ServiceFrontController.getInstance().fireCommand(_context,
                    RequestEnum.updateStatus.toString(), pramas, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);

                            if (responceObj.isStatus()) {

                            }
                        }

                        @Override
                        public void fault(String message) {

                        }
                    }, RequestMethod.GET);
        } catch (Exception e) {
        }

    }


    public void getStatus(User user) {

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.getStatus.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "status/getUserStatus");

        pramas.put("userID", user.getId());
        final User userObj = user;
        try {
            ServiceFrontController.getInstance().fireCommand(_context,
                    RequestEnum.getStatus.toString(), pramas, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);

                            if (responceObj.isStatus()) {

                                Gson gson = new Gson();
                                Status status = new Status();

                                status = (Status) gson.fromJson(responceObj.getDataString(),
                                        new TypeToken<Status>() {
                                        }.getType());

                                StatusDAO statusDAO = new StatusDAO();
                                statusDAO.save(status);

                                userObj.setStatusMsg(status.getStatusMsg());
                                MyApplication.getInstance().getPrefManager().storeUser(userObj);
                            }
                        }

                        @Override
                        public void fault(String message) {

                        }
                    }, RequestMethod.GET);
        } catch (Exception e) {
        }

    }


    public void getPlayingStatus(User user) {

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.getPlayingStatus.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "status/getPlayingStatus");

        pramas.put("userID", user.getId());

        try {
            ServiceFrontController.getInstance().fireCommand(_context,
                    RequestEnum.getPlayingStatus.toString(), pramas, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);

                            if (responceObj.isStatus()) {

                                Gson gson = new Gson();

                                PlayingStatus status = new PlayingStatus();

                                status = (PlayingStatus) gson.fromJson(responceObj.getDataString(),
                                        new TypeToken<PlayingStatus>() {
                                        }.getType());

                                PlayStatusDAO statusDAO = new PlayStatusDAO();
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


    public void getUser(String userId) {

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.getUser.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "user/getUser");

        pramas.put("userID", userId);

        try {
            ServiceFrontController.getInstance().fireCommand(_context,
                    RequestEnum.getUser.toString(), pramas, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;

                            responceObj = JsonController.getRespnceJson(jsonResult);
                            JsonController<User> jsonController = new JsonController<User>();

                            User user = jsonController
                                    .getModelFromData(responceObj.getData().toString(), User.class);

                            if (responceObj.isStatus()) {
                                Bitmap bitImg = ImageUtils.getImageFrmString(JsonController.getValue(responceObj.getData().toString(), "profilePic"));
                                ImageUtils.saveImage(_context, bitImg, user.getId());
                            }
                        }

                        @Override
                        public void fault(String message) {

                        }
                    }, RequestMethod.GET);
        } catch (Exception e) {
        }

    }

    public void updatePlayingStatus(String playingSrc, String playingInfo, String status) {
        User user = MyApplication.getInstance().getPrefManager().getUser();

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.updatePlayingStatus.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "status/updatePlayingStatus");

        pramas.put("userID", user.getId());
        pramas.put("playingSrc", playingSrc);
        pramas.put("playingInfo", playingSrc);
        pramas.put("status", status);

        try {
            ServiceFrontController.getInstance().fireCommand(_context,
                    RequestEnum.updatePlayingStatus.toString(), pramas, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);

                            if (responceObj.isStatus()) {

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
