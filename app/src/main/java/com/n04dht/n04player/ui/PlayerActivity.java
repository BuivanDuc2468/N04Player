package com.n04dht.n04player.ui;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.n04dht.n04player.R;
import com.n04dht.n04player.databinding.ActivityPlayerBinding;
import com.n04dht.n04player.model.Song;
import com.n04dht.n04player.util.TimeUtil;

import java.io.IOException;
import java.util.List;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    ActivityPlayerBinding activityPlayerBinding;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private int currentSongIndex;
    private List<Song> songList;
    private TimeUtil  timeUtil = new TimeUtil();
    private Song song = new Song();
    private Handler handler = new Handler();
    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            if (mediaPlayer == null) return;
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();
            activityPlayerBinding.songTotalDurationLabel.setText(String.format("%s", timeUtil.milliSecondsToTimer(totalDuration)));
            activityPlayerBinding.songCurrentDurationLabel.setText(String.format("%s", timeUtil.milliSecondsToTimer(currentDuration)));
            int progress = (timeUtil.getProgressPercentage(currentDuration, totalDuration));
            activityPlayerBinding.seekbar.setProgress(progress);
            handler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlayerBinding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        song = (Song) getIntent().getExtras().getSerializable("song");
        activityPlayerBinding.title.setText(song.getTitle());
        activityPlayerBinding.artist.setText(song.getArtist());
        Glide.with(this).load(song.getThubnail()).placeholder(R.drawable
                .default_album_art).error(R.drawable.default_album_art).centerCrop().into(activityPlayerBinding.imagePlayer);
        activityPlayerBinding.playPause.setOnClickListener(this);
        activityPlayerBinding.playBack.setOnClickListener(this);
        activityPlayerBinding.playNext.setOnClickListener(this);
        playSong(song);
    }
    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.play_pause:{
                playMusic();
                Log.e("Error", "error");
//                currentSongIndex = songList.indexOf(song);
//                if(mediaPlayer==null){
////                    try{
////                        mediaPlayer = new MediaPlayer();
////                        mediaPlayer.setDataSource(song.getUrl());
////                        mediaPlayer.prepareAsync();
////                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
////                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
////                            @Override
////                            public void onPrepared(MediaPlayer mp) {
////                                mp.start();
//////                                    int sdk = Build.VERSION.SDK_INT;
//////                                    if(sdk < Build.VERSION_CODES.JELLY_BEAN){
//////                                        activityPlayerBinding.playPause.setBackgroundDrawable(ContextCompat.getDrawable( v.getContext(), R.drawable.pause));
//////                                    }
//////                                    else{
//////                                        activityPlayerBinding.playPause.setBackground(ContextCompat.getDrawable(v.getContext() , R.drawable.pause));
//////                                    }
////                            }
////                        });
////                    } catch (IOException e) {
////                        Log.e("Run","Error");
////                    };
//
//
//                }playMusic();
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

    private void playMusic() {
        if (isPlaying) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                activityPlayerBinding.playPause.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
            }
            isPlaying = false;
            mediaPlayer.start();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activityPlayerBinding.playPause.setBackground(getResources().getDrawable(android.R.drawable.ic_media_play));
        }
        mediaPlayer.pause();
        isPlaying = true;
    }

    public void playSong(Song song) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(song.getUrl());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    isPlaying = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        activityPlayerBinding.playPause.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
                    }
                }
            });

//            activityMainBinding.layoutMedia.setVisibility(View.VISIBLE);
            // set Progress bar values
            activityPlayerBinding.seekbar.setProgress(0);
            activityPlayerBinding.seekbar.setMax(100);
            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
        }
    }
    public void updateProgressBar() {
        handler.postDelayed(updateTimeTask, 100);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(updateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = timeUtil.progressToTimer(seekBar.getProgress(), totalDuration);
        mediaPlayer.seekTo(currentPosition);
        updateProgressBar();
    }
}
