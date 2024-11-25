package com.sai.mechat.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sai.mechat.constants.Patterns;
import com.sai.mechat.functions.Time;
import com.sai.mechat.databinding.FragmentHavenBinding;


public class HavenFragment extends Fragment {
    private FragmentHavenBinding views;


    public HavenFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        views = FragmentHavenBinding.inflate(inflater,container,false);
        Time();
        return views.getRoot();
    }
    public void Time(){
        Handler handler = new Handler();
        handler.postDelayed(() ->{
            views.test.setText(Time.INSTANCE.readableTime(System.currentTimeMillis(), Patterns.TIME_12_HH_MM_SS));
            Time();
        },100);
    }
}