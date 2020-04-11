package com.n04dht.n04player.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.n04dht.n04player.R;
import com.n04dht.n04player.databinding.ActivityMainBinding;
import com.n04dht.n04player.model.Song;
import com.n04dht.n04player.util.MyRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    private List<Song> songList = new ArrayList<>();
    private String [] permissions;
    private static final int PERMISIONS_REQUEST = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.recycleriView.setLayoutManager(new LinearLayoutManager(this));
        activityMainBinding.recycleriView.setHasFixedSize(true);

        getSongList();
        // Sort alphabetically by title
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song lhs, Song rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });

        // Create and set adapter
        MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(this, songList);
        activityMainBinding.recycleriView.setAdapter(myRecyclerAdapter);
        //Check quyen da duoc granted chua
        //Khi quyen bi denied
        permissions = new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.CALL_PHONE};

        for(String permission : permissions) {

            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestStoragePermission(permissions, permission);
            }
            // Khi quyen da granted
            else {

            }
        }
    }
    public void checkPermission(String[] permissions){

    }
    public void requestStoragePermission(String[] permissions, String permission){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,  permission)){

        }
        else{
            ActivityCompat.requestPermissions(this, permissions, PERMISIONS_REQUEST);
        }

    }

    @Override
    public void onRequestPermissionsResult(@NonNull int requestCode,@NonNull String[] permissions, int[] grantResults){
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

               Toast.makeText(this, permissions[0], Toast.LENGTH_SHORT).show();
           }else {
               Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
           }
//       if(requestCode == PERMISIONS_REQUEST){
//           if(grantResults.length>0){
//               String permission_granted = "";
//               for(int i = 0; i<grantResults.length; i++) {
//                   if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                       permission_granted = permission_granted + permissions[i];
//                   }
//               }
//
//               Toast.makeText(this, permission_granted+"Permission GRANTED", Toast.LENGTH_SHORT).show();
//           }
//           else {
//               Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
//           }


//           for(int i : grantResults){
//
//               if(ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED){
//                   requestStoragePermission(permissions, permission);
//               }
//               // Khi quyen da granted
//               else {
//
//               }
//           }
//
//
//
//           for(int i = 0; i<grantResults.length; i++){
//
//               if(grantResults.length>0 && grantResults[i] == PackageManager.PERMISSION_GRANTED){
//
//                   Toast.makeText(this, "Permission GRANDTED", Toast.LENGTH_SHORT).show();
//               }else {
//                   Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
//               }
//
//           }


    }
    public void getSongList() {
// Query external audio resources
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
// Iterate over results if valid
        if (musicCursor != null && musicCursor.moveToFirst()) {
// Get columns
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn =
                    musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do {
                Long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }
}
