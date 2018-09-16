package com.musipo.fcm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.musipo.constant.Config;
import com.musipo.constant.RequestEnum;
import com.musipo.helper.MyApplication;
import com.musipo.model.User;
import com.musipo.model.json.ResponceJson;
import com.musipo.restcall.IServiceListener;
import com.musipo.restcall.JsonController;
import com.musipo.restcall.Request;
import com.musipo.restcall.RequestMethod;
import com.musipo.restcall.ServiceFrontController;

import java.util.HashMap;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        Context _context = getBaseContext();
        try {
            sendRegistrationToServer(refreshedToken,_context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    public static void sendRegistrationToServer(final String token, final Context _context) throws Exception {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

        User user = MyApplication.getInstance().getPrefManager().getUser();

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.updateFcmRegistrationId.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "user/updatefcm");

        pramas.put("userID", user.getId());
        pramas.put("fcmRegistrationID", token);

        Context context = null;
        ServiceFrontController.getInstance().fireCommand(context,
                RequestEnum.updateFcmRegistrationId.toString(), pramas, new IServiceListener() {
                    @Override
                    public void result(String jsonResult) {

                        ResponceJson responceObj = null;
                        responceObj = JsonController.getRespnceJson(jsonResult);

                        if(responceObj.isStatus()) {
                            Intent registrationComplete = new Intent(Config.SENT_TOKEN_TO_SERVER);
                            LocalBroadcastManager.getInstance(_context).sendBroadcast(registrationComplete);
                        }
                    }

                    @Override
                    public void fault(String message) {

                    }
                }, RequestMethod.GET);

    }


    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }


}

