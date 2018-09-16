package com.musipo.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.musipo.R;
import com.musipo.constant.Config;
import com.musipo.fcm.MyFirebaseInstanceIDService;
import com.musipo.fcm.NotificationUtils;
import com.musipo.helper.MyApplication;
import com.musipo.service.LoadUserFromServer;
import com.musipo.service.ServerSyncServices;
import com.musipo.ui.fragment.ChatFragment;
import com.musipo.ui.fragment.GroupChatFragment;
import com.musipo.ui.fragment.PlayMusicFragment;
import com.musipo.ui.fragment.StatusFragment;
import com.musipo.util.MarshmallowPermission;
import com.musipo.util.UiUtils;
import com.musipo.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "bb";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
  //  private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), addFragments());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        broadCastMsg();

        ServerSyncServices serverSyncServices = new ServerSyncServices(this);
        serverSyncServices.updateLastSeen();


        LoadUserFromServer loadUserFromServer = new LoadUserFromServer(this);
        loadUserFromServer.syncUser();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private List<Fragment> addFragments() {
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(new PlayMusicFragment());
        fragmentList.add(new ChatFragment());
        fragmentList.add(new StatusFragment());
        fragmentList.add(new GroupChatFragment());

        return fragmentList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_profile) {
            try {

                com.musipo.util.DatabaseUtils.pullDatabaseFile(this);
                com.musipo.util.DatabaseUtils.insertTempData(this,(Activity)this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_logout) {
            MyApplication.getInstance().getPrefManager().clearData();
            openLoginActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            switch (sectionNumber) {
                case 0:
                    return new PlayMusicFragment();
                case 1:
                    return new ChatFragment();
                case 2:
                    return new StatusFragment();
                case 3:
                    return new GroupChatFragment();
            }
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fmList) {
            super(fm);
            this.mFragmentList = fmList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Play";
                case 1:
                    return "Chat";
                case 2:
                    return "Status";
                case 3:
                    return "Group chat";
            }
            return null;
        }
    }

    private void broadCastMsg() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };

        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);
        try {
          //  sendRegistrationToServer(regId);
            MyFirebaseInstanceIDService.sendRegistrationToServer(regId, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(this);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MarshmallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            MarshmallowPermission marshmallowPermission = new MarshmallowPermission();
            if(marshmallowPermission.checkPermissionForExternalStorage(this)) {
                // can write to external
            } else {
                // runtime permission denied, user must enable permission manually
            }
        }
    }

}
