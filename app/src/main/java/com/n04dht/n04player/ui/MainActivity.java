package com.n04dht.n04player.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.n04dht.n04player.R;
import com.n04dht.n04player.databinding.ActivityMainBinding;
import com.n04dht.n04player.model.Song;
import com.n04dht.n04player.util.MyRecyclerAdapter;
import com.n04dht.n04player.util.PermissionsUtil;
import com.n04dht.n04player.util.RecyclerItemTouchHelper;
import com.n04dht.n04player.util.TimeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private List<Song> songList = new ArrayList<>();
    private int STORAGE_PERMISSION_ID=0;
    private MyRecyclerAdapter myRecyclerAdapter;
    private int currentSongIndex;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = true;
    private TimeUtil timeUtil;
    private static Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
        getSongList();
        setUpAdapter();
    }
    public void init(){
        if (! checkStorePermission(STORAGE_PERMISSION_ID)) {
            showRequestPermission(STORAGE_PERMISSION_ID);

        }
    }
    private boolean checkStorePermission(int permission) {
        if (permission == STORAGE_PERMISSION_ID) {
            return PermissionsUtil.checkPermissions(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            return true;
        }
    }

    private void showRequestPermission(int requestCode) {
        String[] permissions;
        if (requestCode == STORAGE_PERMISSION_ID) {
            permissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
        }
        PermissionsUtil.requestPermissions(this, requestCode, permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 0) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        }
    }
    public void setUpAdapter(){
        myRecyclerAdapter = new MyRecyclerAdapter(this, songList);
        activityMainBinding.recycleriView.setItemAnimator(new DefaultItemAnimator());
        activityMainBinding.recycleriView.setLayoutManager(new LinearLayoutManager(this));
        activityMainBinding.recycleriView.setHasFixedSize(true);
        activityMainBinding.recycleriView.setAdapter(myRecyclerAdapter);
    }
    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC;
        Cursor cursor = musicResolver.query(musicUri, null, selection, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                Uri uri = ContentUris.withAppendedId(sArtworkUri,cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                songList.add(new Song(id,name, title, artist,uri.toString(), url));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song lhs, Song rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
    }
}
