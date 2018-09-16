package com.musipo.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.musipo.R;
import com.musipo.helper.MyApplication;
import com.musipo.model.User;
import com.musipo.service.ServerSyncServices;
import com.musipo.util.ImageUtils;
import com.musipo.util.LocalPersistence;

public class ChatProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    int mutedColor = R.attr.colorPrimary;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Intent intent = getIntent();
        String chatUserId = intent.getStringExtra("user_id");

        User user = MyApplication.getInstance().getPrefManager().getUser();

        Bitmap imageBit = ImageUtils.getImageBitmap(getApplicationContext(), user.getMobile());
        ImageView imageView = (ImageView)findViewById(R.id.image);
        imageView.setImageBitmap(imageBit);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String title = intent.getStringExtra("name");
        getSupportActionBar().setTitle(title);
        toolbar.setTitle(title);
        collapsingToolbarLayout.setTitle(title);
        getStatus();
    }

    private void getStatus(){

        ServerSyncServices serverSyncServices =  new ServerSyncServices(this);
        serverSyncServices.updateLastSeen();
        User user = MyApplication.getInstance().getPrefManager().getUser();
        serverSyncServices.getStatus(user);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }
}
