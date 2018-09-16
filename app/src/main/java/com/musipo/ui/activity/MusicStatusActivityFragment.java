package com.musipo.ui.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musipo.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MusicStatusActivityFragment extends Fragment {

    public MusicStatusActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_status, container, false);
    }
}
