package com.musipo.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musipo.R;
import com.musipo.adapter.StatusListAdapter;
import com.musipo.dao.impl.PlayStatusDAO;
import com.musipo.helper.SimpleDividerItemDecoration;
import com.musipo.model.PlayingStatus;
import com.musipo.ui.activity.MusicStatusActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {


    private View rootView;
    private RecyclerView recyclerView;
    private Context _context;
    private ArrayList<PlayingStatus> playingStatusArrayList;

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_status, container,
                false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        init();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), MusicStatusActivity.class);
                startActivity(in);
            }
        });
        return rootView;
    }

    private void init() {
        _context = getActivity();
        addUserAdapter();
    }

    private void addUserAdapter() {

        playingStatusArrayList = new ArrayList<>();

        PlayStatusDAO playStatusDAO = new PlayStatusDAO();
        List<PlayingStatus> playingStatusList = playStatusDAO.findAll();
        if (playingStatusList != null) {
            playingStatusArrayList.addAll(playingStatusList);
        }
        StatusListAdapter statusListAdapter =  new StatusListAdapter(_context.getApplicationContext(),playingStatusArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(_context.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(_context.getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(statusListAdapter);
    }





}
