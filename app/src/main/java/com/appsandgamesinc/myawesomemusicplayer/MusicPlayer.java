package com.appsandgamesinc.myawesomemusicplayer;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MusicPlayer extends AppCompatActivity
{

    SeekBar seekBar;
    ImageView play,pause,next,back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        MediaPlayer music;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        play = (ImageView) findViewById(R.id.ivPlay);
        pause = (ImageView) findViewById(R.id.ivPause);
        next = (ImageView) findViewById(R.id.ivNext);
        back = (ImageView) findViewById(R.id.ivBack);


        String songName = getIntent().getStringExtra("name");
        String artistName = getIntent().getStringExtra("artist");
        int cover = getIntent().getIntExtra("image",0);
        int song = getIntent().getIntExtra("song",0);

        TextView tvName = (TextView) findViewById(R.id.tvSongName);
        TextView tvArtist = (TextView) findViewById(R.id.tvSongArtist);
        ImageView ivCover = (ImageView) findViewById(R.id.ivCover);

        tvName.setText(songName);
        tvArtist.setText(artistName);
        ivCover.setImageResource(cover);

    }
}
