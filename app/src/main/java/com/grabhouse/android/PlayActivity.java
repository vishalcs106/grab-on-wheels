package com.grabhouse.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.grabhouse.android.Models.Property;

import java.util.ArrayList;

import io.paperdb.Paper;

public class PlayActivity extends Activity{
    ImageView youTubeView;
    ImageView addToSmart;
    Context context;
    Property property;
    String imgURL;
    TextView textViewP, textViewA, textViewT;
    @Override
    protected void onStart() {
        super.onStart();


       // Paper.book().write("loclist", new ArrayList<String>());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Paper.init(this);
        textViewP = (TextView) findViewById(R.id.priceText);
        textViewA = (TextView) findViewById(R.id.addText);
        addToSmart = (ImageView) findViewById(R.id.addTo);
        textViewT = (TextView) findViewById(R.id.typeText);
        youTubeView = (ImageView) findViewById(R.id.youtube_view);
        context = this;
        imgURL = getIntent().getStringExtra("img");
        property = getIntent().getParcelableExtra("propObj");
        textViewP.setText("Price: "+property.mValue);
        textViewA.setText("Location: "+property.mName);
        textViewT.setText(property.mType);
        Glide.with(context).load(imgURL).into(youTubeView);
        addToSmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Added to Smart Route", Toast.LENGTH_LONG).show();
                String latlng = getIntent().getStringExtra("latlng");
                ArrayList<String> as = Paper.book().read("loclist",new ArrayList<String>());
                as.add(latlng);
                Log.d("Loclat list",as.toString());
                Paper.book().write("loclist",as);
            }
        });
    }


}
