package com.appsandgamesinc.myawesomemusicplayer;


import android.app.DownloadManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URLEncoder;

public class MusicPlayer extends AppCompatActivity
{

    private SeekBar seekBar;
    private ImageView play, next, back;
    private TextView currentTime, songDuration,tvLyrics;
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
        tvLyrics = (TextView) findViewById(R.id.tvLyrics);
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
        Picasso.with(MusicPlayer.this).load(getString(cover)).into(ivCover);


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
        getTrackId(songName, artistName);

    }

    private void getTrackId(final String track, String artist)
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.musixmatch_baseurl) + "track.search?apikey=" + getString(R.string.musixmatch_apikey) + "&q_track=" + track + "&q_artist=" + artist + "&page_size=20";
        url = url.replaceAll(" ", "%20");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getJSONObject("message").getJSONObject("header").has("status_code"))
                    {
                        int statusCode = obj.getJSONObject("message").getJSONObject("header").getInt("status_code");

                        if (statusCode == getResources().getInteger(R.integer.musixmatch_request_successful))
                        {

                            JSONObject bodyObj = obj.getJSONObject("message").getJSONObject("body");
                            JSONArray trackListArray = bodyObj.getJSONArray("track_list");
                            JSONObject trackObj = trackListArray.getJSONObject(0).getJSONObject("track");

                            getLyrics(trackObj.getInt("track_id"));

                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_bad_syntax))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_bad_syntax), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_authentication_failed))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_authentication_failed), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_usage_limit))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_usage_limit), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_not_authorized))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_not_authorized), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_resource_not_found))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_resource_not_found), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_method_not_found))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_method_not_found), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_something_wrong))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_system_busy))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_system_busy), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(MusicPlayer.this, "Sorry. Incorrect response.", Toast.LENGTH_SHORT).show();
                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("get song id error", error.toString());

            }
        });

        queue.add(stringRequest);
    }


    private void getLyrics(int trackId)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.musixmatch_baseurl) + "track.lyrics.get?track_id=" + trackId + "&apikey=" + getString(R.string.musixmatch_apikey);
        url = url.replaceAll(" ", "%20");


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getJSONObject("message").getJSONObject("header").has("status_code"))
                    {
                        int statusCode = obj.getJSONObject("message").getJSONObject("header").getInt("status_code");

                        if (statusCode == getResources().getInteger(R.integer.musixmatch_request_successful))
                        {
                            JSONObject bodyObj = obj.getJSONObject("message").getJSONObject("body").getJSONObject("lyrics");
                            tvLyrics.setText(bodyObj.getString("lyrics_body"));
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_bad_syntax))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_bad_syntax), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_authentication_failed))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_authentication_failed), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_usage_limit))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_usage_limit), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_not_authorized))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_not_authorized), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_resource_not_found))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_resource_not_found), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_method_not_found))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_method_not_found), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_something_wrong))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                        else if (statusCode == getResources().getInteger(R.integer.musixmatch_request_system_busy))
                        {
                            Toast.makeText(MusicPlayer.this, getString(R.string.musixmatch_request_system_busy), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(MusicPlayer.this, "Sorry. Incorrect response.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("get song id error", error.toString());

            }
        });

        queue.add(stringRequest);
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

