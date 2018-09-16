package com.musipo.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musipo.R;
import com.musipo.ui.activity.GroupActivity;
import com.musipo.ui.activity.UserContactListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment {


    private View rootView;

    public GroupChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_group_chat, container,
                false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), GroupActivity.class);
                startActivity(in);
            }
        });

        return rootView;
    }

}
