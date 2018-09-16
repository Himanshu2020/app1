package com.musipo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.musipo.R;
import com.musipo.adapter.ChatRoomsAdapter;
import com.musipo.adapter.UserListAdapter;
import com.musipo.dao.impl.ChatRoomDao;
import com.musipo.dao.impl.UserDao;
import com.musipo.helper.SimpleDividerItemDecoration;
import com.musipo.model.ChatRoom;
import com.musipo.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserContactListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> userArrayList;
    private Context _context;
    private UserListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contact_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pro1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        init();
    }

    private void init() {
        _context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        addUserAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addUserAdapter() {
        //fetchChatRooms();
        userArrayList = new ArrayList<>();

        UserDao userDao = new UserDao();

        List<User> userList = userDao.findAll();
        userArrayList.addAll(userList);

        mAdapter = new UserListAdapter(_context.getApplicationContext(), userArrayList);
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
                User user = userArrayList.get(position);
                Intent intent = new Intent(UserContactListActivity.this, ChatRoomActivity.class);

                ChatRoomDao chatRoomDao = new ChatRoomDao();
                ChatRoom chatRoom = chatRoomDao.getChatRoomByUser(user);

                if (chatRoom == null) {
                    intent.putExtra("chat_room_id", "");
                } else {
                    intent.putExtra("chat_room_id", chatRoom.getId());
                }
              //  intent.putExtra("chat_room_id", "");
                intent.putExtra("name", user.getName());
                intent.putExtra("user_id", user.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


}
