package com.musipo.ui.fragment;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.musipo.R;
import com.musipo.service.ServerSyncServices;
import com.musipo.ui.activity.ChatProfileActivity;
import com.musipo.ui.activity.PlayListActivity;
import com.musipo.ui.activity.UserContactListActivity;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicFragment extends Fragment {

    static MediaPlayer mPlayer;
    Button buttonPlay;
    Button buttonStop;
   final String url = "http://android.programmerguru.com/wp-content/uploads/2013/04/hosannatelugu.mp3";
    private View rootView;


    public PlayMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_play_music, container,
                false);
        init();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), PlayListActivity.class);
                startActivity(in);
            }
        });
        return rootView;
    }

    private final void init() {

        buttonPlay = (Button) rootView.findViewById(R.id.play);



        buttonPlay.setOnClickListener(new View.OnClickListener() {



            public void onClick(View v) {

                updatePlayStatus();

                mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mPlayer.setDataSource(url);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (SecurityException e) {
                    Toast.makeText(getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IllegalStateException e) {
                    Toast.makeText(getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mPlayer.prepare();
                } catch (IllegalStateException e) {
                    Toast.makeText(getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                }
                mPlayer.start();
            }
        });





        buttonStop = (Button) rootView.findViewById(R.id.stop);
        buttonStop.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
            }
        });

    }

    private void updatePlayStatus() {
        final ServerSyncServices serverSyncServices = new ServerSyncServices();
        final String  playinfo = "Inna Sona tenu";
        serverSyncServices.updatePlayingStatus(url,playinfo,playinfo);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }


}
