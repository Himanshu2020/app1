package com.musipo.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.musipo.R;
import com.musipo.fcm.MyFirebaseInstanceIDService;
import com.musipo.helper.MyApplication;
import com.musipo.model.User;
import com.musipo.service.LoadUserFromServer;
import com.musipo.util.DatabaseUtils;


public class SplashActivity extends Activity {
    private static final int SPLASH_TIME_OUT = 900;
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);
        super.onCreate(savedInstanceState);

        displaySplashScreen();

        DatabaseUtils.initializeDatabase(getApplicationContext(), false);

        LoadUserFromServer loadUserFromServer =  new LoadUserFromServer(this);
        loadUserFromServer.syncUser();
       try {
            loadUserFromServer.fetchChatRooms(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void openMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void openLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void displaySplashScreen() {
        new Handler().postDelayed(new Runnable() {

			/*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {

                User user = MyApplication.getInstance().getPrefManager().getUser();
             //   if (user != null) {
                if (user !=null&&!user.getId().isEmpty()) {
                    openMainActivity();
                    Log.i(TAG, "User session is running");
                } else {
                    openLoginActivity();
                    Log.i(TAG, "User session is not running");
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}
