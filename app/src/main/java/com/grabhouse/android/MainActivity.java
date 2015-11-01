package com.grabhouse.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.goebl.david.Webb;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.grabhouse.android.Models.Property;
import com.grabhouse.android.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class MainActivity extends YouTubeBaseActivity implements View.OnTouchListener{

    private GoogleMap googleMap;
    double latitude,longitude ;
    LinearLayout bottomLayout;
    Context context;
    YouTubePlayerView youTubeView;
    ViewGroup toolBarLayout;
    YouTubePlayer player;
    ImageView toggleMap,searchMap;
    RelativeLayout mapLayout;
    ListView listView;
    int cnt = 0;
    boolean mapSeen = true;
    int rid = 0;
    ArrayList<Property> properties;
    boolean touchedUp = true;
    private static final String TAG = "polygon";
    private GoogleMap mGoogleMap;
    private View mMapShelterView;
    private GestureDetector mGestureDetector;
    private ArrayList<LatLng> mLatlngs = new ArrayList<LatLng>();
    private PolylineOptions mPolylineOptions;
    private PolygonOptions mPolygonOptions;
    RelativeLayout mapDraw;
    ImageView stNav;
    // flag to differentiate whether user is touching to draw or not
    private boolean mDrawFinished = false;
    private LinearLayout listOfHouses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        context = this;
        listView = (ListView) findViewById(R.id.listView);
        mapLayout = (RelativeLayout) findViewById(R.id.mapLayout);
        properties = new ArrayList<>();
        toolBarLayout = (ViewGroup) findViewById(R.id.toolbar);
        toggleMap = (ImageView) toolBarLayout.findViewById(R.id.toggleMap);
        searchMap = (ImageView) toolBarLayout.findViewById(R.id.searchMap);
        stNav =  (ImageView) toolBarLayout.findViewById(R.id.stNav);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.setVisibility(View.GONE);
        mapDraw = (RelativeLayout) mapLayout.findViewById(R.id.mapDraw);
        fetchNearByPlaces();
        mMapShelterView = (View) findViewById(R.id.drawer_view);
        mGestureDetector = new GestureDetector(this, new GestureListener());
        mMapShelterView.setOnTouchListener(this);
        try {
            GPSTracker gps = new GPSTracker(this);
            if(gps.canGetLocation()){
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                // \n is for new line
               // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            }else{
                gps.showSettingsAlert();
            }
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(new LatLng(latitude,longitude)).title("TutorialsPoint"));

//            new Background().execute().get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        searchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
               // startActivity(intent);
            }
        });
        toggleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mapSeen) {
//                    mapLayout.setVisibility(View.GONE);
//                    listView.setVisibility(View.VISIBLE);
//                    mapSeen = false;
//                } else {
//                    mapLayout.setVisibility(View.VISIBLE);
//                    listView.setVisibility(View.GONE);
//                    mapSeen = true;
//                }
            }
        });
        stNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SmartRouterActivity.class);
                startActivity(intent);
            }
        });


    }

    private void fetchNearByPlaces() {
        String url = "http://128.199.157.171:5555/get_near_me";



        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("", response.toString());
                        try {
                        for (int i=0;i<response.length();i++) {

                                JSONObject obj = (JSONObject) response.get(i);
                            JSONObject loc = obj.getJSONObject("loc");
                            String lat = loc.getString("lat");
                            String lon = loc.getString("lon");
                            String type = obj.getString("type");
                            String name = obj.getString("name");
                            String image = obj.getString("image");
                            String isVer = obj.getString("is_verified");
                            String price = obj.getString("price");
                            Property property = new Property();
                            property.mName = name;
                            property.mType = type;
                            property.mLat = lat;
                            property.mLng = lon;
                            property.mVideoID = "pMeP9JOdhHY";
                            property.mValue = price;
                            property.mImages[0] = image;
                            properties.add(property);
                        }
                            PropertyListAdapter pla = new PropertyListAdapter((Activity) context, properties);
                            listView.setAdapter(pla);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(context, PlayActivity.class);
                                    Log.d("latlng",properties.get(position).mLat);
                                    intent.putExtra("latlng",properties.get(position).mLat );
                                    startActivity(intent);
                                }
                            });

                            HorizontalListViewAdapter hLA = new HorizontalListViewAdapter((Activity) context, properties);
                            bottomLayout.addView(hLA.getView());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());

            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_bus_near_me, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void initYoutube() {
        youTubeView.setVisibility(View.VISIBLE);
        youTubeView.initialize(Constants.API_KEY, new MyYoutubeListener());
    }

    public void releaseYT() {
        youTubeView.setVisibility(View.GONE);
        try {
            player.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyYoutubeListener implements YouTubePlayer.OnInitializedListener {
       @Override
       public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
           player = youTubePlayer;
           if (!wasRestored) {
               youTubePlayer.loadVideo(Constants.VIDEOS[0]);
               youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
           }
       }

       @Override
       public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
           if (youTubeInitializationResult.isUserRecoverableError()) {
               youTubeInitializationResult.getErrorDialog((Activity) context, 1).show();
           } else {
               //Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
           }
       }


   }
    private class Background extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Webb webb = Webb.create();
            JSONObject array = webb.get("http://ec2-52-24-94-113.us-west-2.compute.amazonaws.com:5000/get_buses/865800024106160/"+latitude+":"+longitude).asJsonObject().getBody();
            Iterator<String> iter = array.keys();
            int i = 0;
            while (iter.hasNext()) {
                String key = iter.next();
                i++;
                try {
                    JSONArray values = array.getJSONArray(key);
//                    String[] stringArray = Arrays.copyOf(values, values.length, String[].class);
                    final double lats =Double.parseDouble(values.get(0).toString());
                    final double lngs = Double.parseDouble(values.get(1).toString());

                    Log.d("Object Near me", values.toString());
                    final int finalI = i;
                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(finalI %2==0) {
                                googleMap.addMarker(new MarkerOptions().
                                                position(new LatLng(lats, lngs)).title("Bus Name: " + "500D").snippet("Type: " + "Volvo")
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_filled))
                                );
                            }else{
                                googleMap.addMarker(new MarkerOptions().
                                                position(new LatLng(lats, lngs)).title("Bus Name: " + "502E").snippet("Type: " + "Normal")
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trolleybus))
                                );
                            }
                        }
                    });

                } catch (JSONException e) {

                }
            }
            return null;
        }
    }
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }
    }

    /**
     * Ontouch event will draw poly line along the touch points
     *
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int X1 = (int) event.getX();
        int Y1 = (int) event.getY();
        Point point = new Point();
        point.x = X1;
        point.y = Y1;
        LatLng firstGeoPoint = googleMap.getProjection().fromScreenLocation(
                point);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                if (mDrawFinished) {
                    X1 = (int) event.getX();
                    Y1 = (int) event.getY();
                    point = new Point();
                    point.x = X1;
                    point.y = Y1;
                    LatLng geoPoint = googleMap.getProjection()
                            .fromScreenLocation(point);
                    mLatlngs.add(geoPoint);
                    mPolylineOptions = new PolylineOptions();
                    mPolylineOptions.color(Color.BLACK);
                    mPolylineOptions.width(7);
                    mPolylineOptions.addAll(mLatlngs);
                    googleMap.addPolyline(mPolylineOptions);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "Poinnts array size " + mLatlngs.size());
                mLatlngs.add(firstGeoPoint);
                googleMap.clear();
                mPolylineOptions = null;
                mMapShelterView.setVisibility(View.GONE);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                googleMap.getUiSettings().setAllGesturesEnabled(true);
                mPolygonOptions = new PolygonOptions();
                mPolygonOptions.fillColor(0x20FF0000);
                mPolygonOptions.strokeColor(Color.BLACK);
                mPolygonOptions.strokeWidth(7);
                mPolygonOptions.addAll(mLatlngs);
                googleMap.addPolygon(mPolygonOptions);
                mDrawFinished = false;
                if(mLatlngs.size()>10) {
                    String mainStr = "";
                    for (int i = 0; i < mLatlngs.size(); i++) {
                        LatLng ll = (LatLng) mLatlngs.get(i);
                        String data = ll.longitude + "," + ll.latitude;
                        mainStr = mainStr + data + "<=>";
                    }
                    try {
                        new DupBackground().execute(new String[]{mainStr}).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * Setting up map
     * Setting up mapmel
     *
     */

    private void initilizeMap() {
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
                googleMap.setMyLocationEnabled(true);
                LatLng coordinate = new LatLng(12.9330105,77.6099673);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 19);
                googleMap.animateCamera(yourLocation);
            }

        } else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            // showErrorDialog(status);
        } else {
            Toast.makeText(this, "No Support for Google Play Service",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method gets called on tap of draw button, It prepares the screen to draw
     * the polygon
     *
     * @param view
     */

    public void drawZone(View view) {
        googleMap.clear();
        mLatlngs.clear();
        mPolylineOptions = null;
        mPolygonOptions = null;
        mDrawFinished = true;
        mMapShelterView.setVisibility(View.VISIBLE);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    public synchronized boolean Contains(Location location) {
        boolean isInside = false;
        if (mLatlngs.size() > 0) {
            LatLng lastPoint = mLatlngs.get(mLatlngs.size() - 1);

            double x = location.getLongitude();

            for (LatLng point : mLatlngs) {
                double x1 = lastPoint.longitude;
                double x2 = point.longitude;
                double dx = x2 - x1;

                if (Math.abs(dx) > 180.0) {
                    if (x > 0) {
                        while (x1 < 0)
                            x1 += 360;
                        while (x2 < 0)
                            x2 += 360;
                    } else {
                        while (x1 > 0)
                            x1 -= 360;
                        while (x2 > 0)
                            x2 -= 360;
                    }
                    dx = x2 - x1;
                }

                if ((x1 <= x && x2 > x) || (x1 >= x && x2 < x)) {
                    double grad = (point.latitude - lastPoint.latitude) / dx;
                    double intersectAtLat = lastPoint.latitude
                            + ((x - x1) * grad);

                    if (intersectAtLat > location.getLatitude())
                        isInside = !isInside;
                }
                lastPoint = point;
            }
        }

        return isInside;
    }
    private class DupBackground extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... voids) {
            Webb webb = Webb.create();
            JSONArray obj = webb.post("http://128.199.157.171:5555/get_places_in_area").param("area",voids[0]).ensureSuccess().asJsonArray().getBody();
            for(int i=0;i<obj.length();i++){
                try {
                    final double lats =Double.parseDouble(obj.getJSONObject(i).getString("lat"));
                    final double lngs = Double.parseDouble(obj.getJSONObject(i).getString("lng"));
                    final String name = obj.getJSONObject(i).getString("name");
                    final String price = obj.getJSONObject(i).getString("price");
                    final String type = obj.getJSONObject(i).getString("type");
//                    if(type.equals("1BHK")) {
//
//                    }

                    switch (type) {
                        case "1BHK" :
                            rid = R.drawable.onebhk_selected;
                            break;
                        case "2BHK" :
                            rid = R.drawable.twobhk_selected;
                            break;
                        case "3BHK" :
                            rid = R.drawable.tribhk_selected;
                            break;
                        default: rid = R.drawable.onebhk_selected;
                    }
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(lats, lngs)).title(name)
                                    .snippet("Price: "+price).icon(BitmapDescriptorFactory.fromResource(rid)));

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Obj",obj.toString());
            return null;
        }
    }
}
