package com.n04dht.n04player.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.n04dht.n04player.R;
import com.n04dht.n04player.databinding.ActivityPlayerBinding;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {
    public ObservableField<String> Ititle = new ObservableField<>();
    public ObservableField<String> Iartist = new ObservableField<>();
    ActivityPlayerBinding activityPlayerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlayerBinding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        Bundle bundle = getIntent().getExtras();
        activityPlayerBinding.title.setText(bundle.getString("Name"));
        activityPlayerBinding.artist.setText(bundle.getString("Artist"));
        activityPlayerBinding.playPause.setOnClickListener(this);
        activityPlayerBinding.playBack.setOnClickListener(this);
        activityPlayerBinding.playNext.setOnClickListener(this);
//        setContentView(R.layout.activity_player);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_pause:{
                Toast.makeText(this, "You have clicked play", Toast.LENGTH_SHORT).show();
                break;

            }
            case  R.id.play_next:{
                Toast.makeText(this, "You have clicked next", Toast.LENGTH_SHORT).show();
                break;

            }
            case R.id.play_back:{
                Toast.makeText(this, "You have clicked back", Toast.LENGTH_SHORT).show();
                break;

            }
        }


    }
}
