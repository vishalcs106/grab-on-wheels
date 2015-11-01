package com.grabhouse.android;

import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.goebl.david.Webb;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * Created by ckasturi on 10/31/15.
 */
public class DupMainActivity extends FragmentActivity// implements View.OnTouchListener
{

    private static final String TAG = "polygon";
    private GoogleMap mGoogleMap;
    private View mMapShelterView;
    private GestureDetector mGestureDetector;
    private ArrayList<LatLng> mLatlngs = new ArrayList<LatLng>();
    private PolylineOptions mPolylineOptions;
    private PolygonOptions mPolygonOptions;
    // flag to differentiate whether user is touching to draw or not
    private boolean mDrawFinished = false;
    private LinearLayout listOfHouses;
    private HorizontalScrollView hrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dup_activity_main);
//
//        mMapShelterView = (View) findViewById(R.id.drawer_view);
//        mGestureDetector = new GestureDetector(this, new GestureListener());
//        mMapShelterView.setOnTouchListener(this);
//        listOfHouses = (LinearLayout)findViewById(R.id.listOfHouses);
//        hrList = (HorizontalScrollView)findViewById(R.id.hrList);
//        initilizeMap();
    }

//    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                               float velocityY) {
//            return false;
//        }
//    }
//
//    /**
//     * Ontouch event will draw poly line along the touch points
//     *
//     */
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int X1 = (int) event.getX();
//        int Y1 = (int) event.getY();
//        Point point = new Point();
//        point.x = X1;
//        point.y = Y1;
//        LatLng firstGeoPoint = mGoogleMap.getProjection().fromScreenLocation(
//                point);
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                if (mDrawFinished) {
//                    X1 = (int) event.getX();
//                    Y1 = (int) event.getY();
//                    point = new Point();
//                    point.x = X1;
//                    point.y = Y1;
//                    LatLng geoPoint = mGoogleMap.getProjection()
//                            .fromScreenLocation(point);
//                    mLatlngs.add(geoPoint);
//                    mPolylineOptions = new PolylineOptions();
//                    mPolylineOptions.color(Color.BLACK);
//                    mPolylineOptions.width(7);
//                    mPolylineOptions.addAll(mLatlngs);
//                    mGoogleMap.addPolyline(mPolylineOptions);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.d(TAG, "Poinnts array size " + mLatlngs.size());
//                mLatlngs.add(firstGeoPoint);
//                mGoogleMap.clear();
//                mPolylineOptions = null;
//                mMapShelterView.setVisibility(View.GONE);
//                mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
//                mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
//                mPolygonOptions = new PolygonOptions();
//                mPolygonOptions.fillColor(0x20FF0000);
//                mPolygonOptions.strokeColor(Color.BLACK);
//                mPolygonOptions.strokeWidth(7);
//                mPolygonOptions.addAll(mLatlngs);
//                mGoogleMap.addPolygon(mPolygonOptions);
//                mDrawFinished = false;
//                if(mLatlngs.size()>40) {
//                    String mainStr = "";
//                    for (int i = 0; i < mLatlngs.size(); i++) {
//                        LatLng ll = (LatLng) mLatlngs.get(i);
//                        String data = ll.longitude + "," + ll.latitude;
//                        mainStr = mainStr + data + "<=>";
//                    }
//                    try {
//                        new Background().execute(new String[]{mainStr}).get();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//        }
//        return mGestureDetector.onTouchEvent(event);
//    }
//
//    /**
//     * Setting up map
//     *
//     */
//
//    private void initilizeMap() {
//        int status = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(getApplicationContext());
//        if (status == ConnectionResult.SUCCESS) {
//            if (mGoogleMap == null) {
//                mGoogleMap = ((SupportMapFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.map)).getMap();
//                mGoogleMap.setMyLocationEnabled(true);
//                LatLng coordinate = new LatLng(12.9330105,77.6099673);
//                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 19);
//                mGoogleMap.animateCamera(yourLocation);
//            }
//
//        } else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
//            // showErrorDialog(status);
//        } else {
//            Toast.makeText(this, "No Support for Google Play Service",
//                    Toast.LENGTH_LONG).show();
//        }
//    }
//
//    /**
//     * Method gets called on tap of draw button, It prepares the screen to draw
//     * the polygon
//     *
//     * @param view
//     */
//
//    public void drawZone(View view) {
//        mGoogleMap.clear();
//        mLatlngs.clear();
//        mPolylineOptions = null;
//        mPolygonOptions = null;
//        mDrawFinished = true;
//        mMapShelterView.setVisibility(View.VISIBLE);
//        mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
//        hrList.setVisibility(View.GONE);
//        listOfHouses.removeAllViews();
//    }
//
//    public synchronized boolean Contains(Location location) {
//        boolean isInside = false;
//        if (mLatlngs.size() > 0) {
//            LatLng lastPoint = mLatlngs.get(mLatlngs.size() - 1);
//
//            double x = location.getLongitude();
//
//            for (LatLng point : mLatlngs) {
//                double x1 = lastPoint.longitude;
//                double x2 = point.longitude;
//                double dx = x2 - x1;
//
//                if (Math.abs(dx) > 180.0) {
//                    if (x > 0) {
//                        while (x1 < 0)
//                            x1 += 360;
//                        while (x2 < 0)
//                            x2 += 360;
//                    } else {
//                        while (x1 > 0)
//                            x1 -= 360;
//                        while (x2 > 0)
//                            x2 -= 360;
//                    }
//                    dx = x2 - x1;
//                }
//
//                if ((x1 <= x && x2 > x) || (x1 >= x && x2 < x)) {
//                    double grad = (point.latitude - lastPoint.latitude) / dx;
//                    double intersectAtLat = lastPoint.latitude
//                            + ((x - x1) * grad);
//
//                    if (intersectAtLat > location.getLatitude())
//                        isInside = !isInside;
//                }
//                lastPoint = point;
//            }
//        }
//
//        return isInside;
//    }
//        private class Background extends AsyncTask<String,Void,Void> {
//
//        @Override
//        protected Void doInBackground(String... voids) {
//            Webb webb = Webb.create();
//            JSONArray obj = webb.post("http://128.199.157.171:5555/get_places_in_area").param("area",voids[0]).ensureSuccess().asJsonArray().getBody();
//            for(int i=0;i<obj.length();i++){
//                try {
//                    final double lats =Double.parseDouble(obj.getJSONObject(0).getString("lat"));
//                    final double lngs = Double.parseDouble(obj.getJSONObject(0).getString("lng"));
//                    DupMainActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            hrList.setVisibility(View.VISIBLE);
//                            ImageButton ib =  new ImageButton(DupMainActivity.this);
//                            ib.setImageResource(R.drawable.bus_filled);
//
//                            listOfHouses.addView(ib);
//                            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lats,lngs)).title("Melbourne")
//                                    .snippet("Population: 4,137,400").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_filled)));
//
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            Log.d("Obj",obj.toString());
//            return null;
//        }
//    }
}
