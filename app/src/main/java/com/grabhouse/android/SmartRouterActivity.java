package com.grabhouse.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.HorizontalScrollView;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.grabhouse.android.com.grabhouse.android.utils.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

/**
 * Created by ckasturi on 10/31/15.
 */
public class SmartRouterActivity extends FragmentActivity{

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
    private ArrayList<LatLng> latLngList = new ArrayList<LatLng>();
    static {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_router);
        Paper.init(this);
        mMapShelterView = (View) findViewById(R.id.drawer_view);
        Webb w = Webb.create();
        ArrayList<String> as = Paper.book().read("loclist");
        Paper.book().delete("loclist");
        String st = "";
        try {
            String joined = TextUtils.join("::", as);
            Log.d("Join", joined);
            JSONArray arr = w.post("http://128.199.157.171:5555/get_smart_route").param("latlongs", joined).ensureSuccess().asJsonArray().getBody();
            for (int i = 0; i < arr.length(); i++) {
                Log.d("Inside arr", arr.toString());
                try {
                    final double lngs = Double.parseDouble(arr.getJSONObject(i).getString("lng"));
                    final double lats = Double.parseDouble(arr.getJSONObject(i).getString("lat"));
                    latLngList.add(new LatLng(lats, lngs));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initilizeMap();
            JSONParser jParser = new JSONParser();
            for (int i = 0; i < latLngList.size(); i++) {
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latLngList.get(i).latitude, latLngList.get(i).longitude)).title("Melbourne")
                        .snippet("Population: 4,137,400").icon(BitmapDescriptorFactory.fromResource(R.drawable.twobhk_selected)));
                if (i < latLngList.size() - 1) {
                    String url = makeURL(latLngList.get(i).latitude, latLngList.get(i).longitude, latLngList.get(i + 1).latitude, latLngList.get(i + 1).longitude);

                    String json = jParser.getJSONFromUrl(url);
                    drawPath(json);
                    Log.d("URL for locatiomn", json);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initilizeMap() {
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        Log.d("TAGS",latLngList.toString());
        if (status == ConnectionResult.SUCCESS) {
            if (mGoogleMap == null) {
                mGoogleMap = ((SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map)).getMap();
                mGoogleMap.setMyLocationEnabled(true);
                LatLng coordinate = new LatLng(12.9330105,77.6099673);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 19);
                mGoogleMap.animateCamera(yourLocation);
            }

        } else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            // showErrorDialog(status);
        } else {
            Toast.makeText(this, "No Support for Google Play Service",
                    Toast.LENGTH_LONG).show();
        }
    }
    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
//        urlString.append("&key=YOUR_API_KEY");
        return urlString.toString();
    }
    public void drawPath(String  result) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(12)
                            .color(Color.parseColor("#05b1fb"))//Google maps blue color
                            .geodesic(true)
            );
           /*
           for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(2)
                .color(Color.BLUE).geodesic(true));
            }
           */
        }
        catch (JSONException e) {

        }
    }
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
}
