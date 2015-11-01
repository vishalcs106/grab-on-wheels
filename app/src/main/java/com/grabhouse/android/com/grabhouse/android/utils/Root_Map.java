package com.grabhouse.android.com.grabhouse.android.utils;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.grabhouse.android.MySupportMapFragment;
import com.grabhouse.android.R;

/**
 * Created by ckasturi on 10/31/15.
 */
public class Root_Map extends FragmentActivity {

    private GoogleMap mMap;
    public static boolean mMapIsTouched = false;
    MySupportMapFragment customMapFragment;
    Projection projection;
    public double latitude;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySupportMapFragment customMapFragment = ((MySupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mMap = customMapFragment.getMap();

        customMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
            @Override
            public void onDrag(MotionEvent motionEvent) {
            Log.i("ON_DRAG", "X:" + String.valueOf(motionEvent.getX()));
            Log.i("ON_DRAG", "Y:" + String.valueOf(motionEvent.getY()));

            float x = motionEvent.getX();
            float y = motionEvent.getY();

            int x_co = Integer.parseInt(String.valueOf(Math.round(x)));
            int y_co = Integer.parseInt(String.valueOf(Math.round(y)));

            projection = mMap.getProjection();
            Point x_y_points = new Point(x_co, y_co);
            LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
            latitude = latLng.latitude;
            longitude = latLng.longitude;

            Log.i("ON_DRAG", "lat:" + latitude);
            Log.i("ON_DRAG", "long:" + longitude);

            // Handle motion event:
        }
        });
    }}
