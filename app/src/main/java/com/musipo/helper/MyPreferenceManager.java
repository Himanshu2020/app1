package com.musipo.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.musipo.model.User;


/**
 * Created by Lincoln on 07/01/16.
 */
public class MyPreferenceManager {


    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "androidhive_gcm";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_USER_MOBILE = "user_mobile";
    private static final String KEY_USER_STATUS = "user_STATUS";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_MOBILE, user.getMobile());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_STATUS, user.getStatusMsg());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences." + user);
    }

    public void storeName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.commit();

        Log.e(TAG, "Name is stored in shared preferences." + name);
    }

    public void storeStatus(String status) {
        editor.putString(KEY_USER_STATUS, status);
        editor.commit();

        Log.e(TAG, "Status is stored in shared preferences." + status);
    }

    public void storeOnSharedPre(String preName, String key, String value) {
        pref = _context.getSharedPreferences(preName, PRIVATE_MODE);
        editor.putString(key, value);
        editor.commit();
    }

    public void clearData() {
        editor.putString(KEY_USER_ID, "");
        editor.putString(KEY_USER_NAME, "");
        editor.putString(KEY_USER_EMAIL, "");
        editor.putString(KEY_USER_MOBILE, "");
        editor.commit();

        Log.e(TAG, "Cleared the shared preference");
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, email, mobile,status;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);
            mobile = pref.getString(KEY_USER_MOBILE, null);
            status = pref.getString(KEY_USER_STATUS, null);

            User user = new User(id, name, email);
            user.setMobile(mobile);
            user.setStatusMsg(status);
            return user;
        }
        return null;
    }


    public String getStatus() {
        String status = pref.getString(KEY_USER_STATUS, null);
        return status;
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
