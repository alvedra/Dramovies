package com.example.h.dramoviesvfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.h.dramoviesvfinal.preferences.MyPreferenceFragment;

public class PrefencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefences);

        getSupportFragmentManager().beginTransaction().add(R.id.setting_holder, new MyPreferenceFragment()).commit();
    }
}
