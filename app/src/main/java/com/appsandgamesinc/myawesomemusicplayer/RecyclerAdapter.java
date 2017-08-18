package com.appsandgamesinc.myawesomemusicplayer;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsandgamesinc.myawesomemusicplayer.model.Music;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>
{
    private ArrayList<Music> musicAL = new ArrayList<>();
    Context context;

    public RecyclerAdapter(ArrayList<Music> musicAL, Context context)
    {
        this.musicAL = musicAL;
        this.context = context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvName;
        public TextView tvArtist;
        public ImageView ivImage;

        public MyViewHolder(View view )
        {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvArtist = (TextView) view.findViewById(R.id.tvArtist);
            ivImage = (ImageView) view.findViewById(R.id.ivImage);
        }

    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position)
    {
        holder.tvName.setText(musicAL.get(position).getName());
        holder.tvArtist.setText(musicAL.get(position).getArtist());
        holder.ivImage.setImageResource(musicAL.get(position).getImageId());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent music = new Intent(context, MusicPlayer.class);
                music.putExtra("name", musicAL.get(position).getName());
                music.putExtra("artist", musicAL.get(position).getArtist());
                music.putExtra("image", musicAL.get(position).getImageId());
                music.putExtra("song", musicAL.get(position).getSongId());
                context.startActivity(music);


            }
        });
    }

    @Override
    public int getItemCount()
    {
        return musicAL.size();
    }
}
