package com.n04dht.n04player.model;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.n04dht.n04player.R;

import java.io.Serializable;

import static androidx.core.content.ContextCompat.getDrawable;

public class Song implements Serializable {
    private long id;
    private String display_name;
    private String title;
    private String artist;
    private String thubnail;
    private String url;


    public Song(){}

    public Song(long id, String display_name, String title, String artist, String thubnail, String url) {
        this.id = id;
        this.display_name = display_name;
        this.title = title;
        this.artist = artist;
        this.thubnail = thubnail;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public String getThubnail() {
        return thubnail;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @BindingAdapter("profileImage")
    public static void loadImage(ImageView view, String thubnail){
        Glide.with(view.getContext()).load(thubnail).placeholder(R.drawable
                .default_album_art).error(R.drawable.default_album_art)
                .centerCrop().into(view);
    }
}
