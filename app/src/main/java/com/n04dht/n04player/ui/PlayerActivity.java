package com.n04dht.n04player.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.n04dht.n04player.R;
import com.n04dht.n04player.databinding.ActivityPlayerBinding;

public class PlayerActivity extends AppCompatActivity {
    ActivityPlayerBinding activityPlayerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlayerBinding = DataBindingUtil.setContentView(this, R.layout.activity_player);
//        setContentView(R.layout.activity_player);
    }

}
