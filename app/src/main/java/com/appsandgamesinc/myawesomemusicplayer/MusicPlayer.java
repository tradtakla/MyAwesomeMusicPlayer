package com.appsandgamesinc.myawesomemusicplayer;


import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MusicPlayer extends AppCompatActivity
{

    private SeekBar seekBar;
    private ImageView play, next, back;
    private TextView currentTime, songDuration;
    private MediaPlayer music;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        play = (ImageView) findViewById(R.id.ivPlay);
        next = (ImageView) findViewById(R.id.ivNext);
        back = (ImageView) findViewById(R.id.ivBack);
        currentTime = (TextView) findViewById(R.id.tvCurrentTime);
        songDuration = (TextView) findViewById(R.id.tvSongDuration);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        play.setOnClickListener(clickListener);
        back.setOnClickListener(clickListener);
        next.setOnClickListener(clickListener);

        String songName = getIntent().getStringExtra("name");
        String artistName = getIntent().getStringExtra("artist");
        int cover = getIntent().getIntExtra("image", 0);
        int song = getIntent().getIntExtra("song", 0);

        TextView tvName = (TextView) findViewById(R.id.tvSongName);
        TextView tvArtist = (TextView) findViewById(R.id.tvSongArtist);
        ImageView ivCover = (ImageView) findViewById(R.id.ivCover);

        tvName.setText(songName);
        tvArtist.setText(artistName);
        ivCover.setImageResource(cover);


        music = MediaPlayer.create(MusicPlayer.this, song);

        songDuration.setText(getTimeString(music.getDuration()));


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                if (music != null && b)
                {
                    music.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

    }

    public static String getTimeString(long duration)
    {
        int minutes = (int) Math.floor(duration / 1000 / 60);
        int seconds = (int) ((duration / 1000) - (minutes * 60));
        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

    private View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.ivPlay:
                    if (music.isPlaying())
                    {
                        play.setImageResource(R.drawable.play_button);
                        music.pause();
                    }
                    else
                    {
                        play.setImageResource(R.drawable.pause_button);
                        music.start();

                        seekBar.setMax(music.getDuration());

                        final Handler handler = new Handler();
                        MusicPlayer.this.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (music != null)
                                {
                                    int currentPosition = music.getCurrentPosition();

                                    seekBar.setProgress(currentPosition);
                                    currentTime.setText(getTimeString(currentPosition));

                                }
                                handler.postDelayed(this, 1000);
                            }
                        });
                    }
                    break;

                case R.id.ivBack:
                    seekBar.setProgress(0);
                    music.seekTo(0);
                    currentTime.setText(getTimeString(music.getCurrentPosition()));
                    break;

                case R.id.ivNext:
                    int maxTime = music.getDuration();
                    seekBar.setProgress(maxTime);
                    music.seekTo(maxTime);
                    currentTime.setText(getTimeString(music.getCurrentPosition()));
                    play.setImageResource(R.drawable.play_button);


//
//                    music.stop();
//                    int time = music.getDuration();
//                    seekBar.setMax(time);
//                    seekBar.setProgress(time);
//                    currentTime.setText(getTimeString(time));
//

                    break;


            }
        }
    };

    @Override
    protected void onPause()
    {
        music.stop();
        MusicPlayer.super.onPause();
    }

}

