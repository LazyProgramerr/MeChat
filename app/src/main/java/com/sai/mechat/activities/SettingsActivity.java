package com.sai.mechat.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sai.mechat.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding views;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(views.getRoot());

    }

}