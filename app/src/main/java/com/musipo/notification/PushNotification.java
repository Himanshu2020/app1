package com.musipo.notification;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by G510 on 25-03-2017.
 */
public class PushNotification {

    public final static String AUTH_KEY_FCM = "AIzaSyBBWoTJ5YLBZ76uXye_l3jyLdOO8I83qxw";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public final static Integer PUSH_FLAG_CHATROOM = 1;
    public final static Integer PUSH_FLAG_USER = 2;
// userDeviceIdKey is the device id you will query from your database

    public static void pushFCMNotification123(final String userDeviceIdKey, final HashMap notificationMsg) throws Exception {

        String authKey = AUTH_KEY_FCM; // You FCM AUTH key
        String FMCurl = API_URL_FCM;

     /*   URL url = new URL(FMCurl);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + authKey);
        conn.setRequestProperty("Content-Type", "application/json");
*/

     /*
     JSONObject info = new JSONObject();
        info.put("title", "Notificatoin Title"); // Notification title
        info.put("body", "Hello Test notification"); // Notification body
*/

/*
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //Your code goes here
                    final JSONObject json = new JSONObject();

                    json.put("to", userDeviceIdKey.trim());
                    json.put("data", notificationMsg);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(json.toString());
                    wr.flush();
                    conn.getInputStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();*/


        // String dataSrt = conn.getResponseCode() + "";
        //  System.out.print("notificationMsg" + notificationMsg + dataSrt);
        JSONObject json = new JSONObject();

    //    pushFCMNotificationData(userDeviceIdKey,notificationMsg);
    }


    private static JSONObject getJsonFromMap(Map<String, Object> map) throws JSONException {
        JSONObject jsonData = new JSONObject();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof Map<?, ?>) {
                value = getJsonFromMap((Map<String, Object>) value);
            }
            jsonData.put(key, value);
        }
        return jsonData;
    }

    // public static void pushFCMNotification(final String DeviceIdKey, final String title, final String body) throws Exception {

    public static void pushFCMNotification(final String DeviceIdKey, final JSONObject notificationMsg) throws Exception {

        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                BufferedReader in = null;


                String authKey = AUTH_KEY_FCM;   // You FCM AUTH key
                String FMCurl = API_URL_FCM;

                URL url = null;
                try {
                    url = new URL(FMCurl);
                } catch (MalformedURLException e) {
                    Log.i("yoyoyo", "error: 1");
                    e.printStackTrace();
                }
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    Log.i("yoyoyo", "error: 2");
                    e.printStackTrace();
                }

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                try {
                    conn.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    Log.i("yoyoyo", "error: 3");
                    e.printStackTrace();
                }
                conn.setRequestProperty("Authorization", "key=" + authKey);
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();
                try {
                    json.put("to", DeviceIdKey.trim());
                } catch (JSONException e) {
                    Log.i("yoyoyo", "error: 4");
                    e.printStackTrace();
                }

                try {
                    json.put("data",notificationMsg);
                } catch (JSONException e) {
                    Log.i("yoyoyo", "error: 7");
                    e.printStackTrace();
                }

                OutputStreamWriter wr = null;
                try {
                    wr = new OutputStreamWriter(conn.getOutputStream());
                } catch (IOException e) {
                    Log.i("yoyoyo", "error: 8");
                    e.printStackTrace();
                }
                try {
                    wr.write(json.toString());
                } catch (IOException e) {
                    Log.i("yoyoyo", "error: 9");
                    e.printStackTrace();
                }
                try {
                    wr.flush();
                } catch (IOException e) {
                    Log.i("yoyoyo", "error: 10");
                    e.printStackTrace();
                }
                try {
                    conn.getInputStream();

                    String dataSrt = conn.getResponseCode()+"";
                    Log.i("yoyoyo", dataSrt);
                } catch (IOException e) {
                    Log.i("yoyoyo", "error: 11");
                    e.printStackTrace();
                }

                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


}
