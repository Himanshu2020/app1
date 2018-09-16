package com.musipo.ui.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.musipo.R;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity {

    static MediaPlayer mPlayer;
    Button buttonPlay;
    Button buttonStop;
    final String url = "http://android.programmerguru.com/wp-content/uploads/2013/04/hosannatelugu.mp3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pro1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }


    private final void init() {
        buttonPlay = (Button) findViewById(R.id.play);

        buttonPlay.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {


                mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mPlayer.setDataSource(url);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(v.getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (SecurityException e) {
                    Toast.makeText(v.getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IllegalStateException e) {
                    Toast.makeText(v.getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mPlayer.prepare();
                } catch (IllegalStateException e) {
                    Toast.makeText(v.getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(v.getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                }
                mPlayer.start();
            }
        });

        buttonStop = (Button) findViewById(R.id.stop);
        buttonStop.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
