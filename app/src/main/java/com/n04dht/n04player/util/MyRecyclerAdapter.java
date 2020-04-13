package com.n04dht.n04player.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.n04dht.n04player.R;
import com.n04dht.n04player.databinding.ItemsLayoutBinding;
import com.n04dht.n04player.model.Song;
import com.n04dht.n04player.ui.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> implements Filterable {

    private List<Song> songList;
    private List<Song> songListFiltered;
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
        holder.itemsLayoutBinding.setImageUrl(song.getThubnail());
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("song", song);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
    public void removeItem(int position) {
        songList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Song item, int position) {
        songList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    songListFiltered = songList;
                } else {
                    List<Song> filteredList = new ArrayList<>();
                    for (Song row : songList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row
                                .getArtist().toLowerCase().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    songListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = songListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                songListFiltered = (ArrayList<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ItemsLayoutBinding itemsLayoutBinding;
        public RelativeLayout viewForeground;

        public MyViewHolder(@NonNull ItemsLayoutBinding itemView) {
            super(itemView.getRoot());
            itemsLayoutBinding = itemView;
            viewForeground = itemView.viewForeground;
        }
    }
}
