package com.appsandgamesinc.myawesomemusicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.appsandgamesinc.myawesomemusicplayer.model.Music;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVars();
    }

    private void initVars()
    {
        ArrayList<Music> musicAL = new ArrayList<>();
        musicAL.add(new Music("Feels","Calvin Harris", R.drawable.a, R.raw.a));
        musicAL.add(new Music("Symphony","Clean Bandit", R.drawable.b,R.raw.b));
        musicAL.add(new Music("Mama","Jonas Blue", R.drawable.c,R.raw.c));
        musicAL.add(new Music("Know No Better","Major Lazer", R.drawable.d,R.raw.d) );
        musicAL.add(new Music("There For You","Martin Garrix & Troye Sivan", R.drawable.e, R.raw.e));
        musicAL.add(new Music("Something Just Like This","The Chainsmokers & Coldplay", R.drawable.f, R.raw.f));


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvMusic);

        RecyclerAdapter mAdapter = new RecyclerAdapter(musicAL, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
