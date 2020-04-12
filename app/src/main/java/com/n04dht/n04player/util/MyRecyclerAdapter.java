package com.n04dht.n04player.util;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.n04dht.n04player.R;
import com.n04dht.n04player.databinding.ItemsLayoutBinding;
import com.n04dht.n04player.model.Song;
import com.n04dht.n04player.ui.PlayerActivity;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<Song> songList;
    private Context context;
    public MyRecyclerAdapter(Context context, List<Song> songList) {
        this.songList = songList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =   LayoutInflater.from(parent.getContext());
        ItemsLayoutBinding itemsLayoutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.items_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemsLayoutBinding);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Song song = songList.get(position);
        holder.itemsLayoutBinding.setSong(song);
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("Title", song.getTitle());
                intent.putExtra("Artist",song.getArtist());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemsLayoutBinding itemsLayoutBinding;

        public MyViewHolder(@NonNull ItemsLayoutBinding itemView) {
            super(itemView.getRoot());
            itemsLayoutBinding = itemView;
        }
    }
}
