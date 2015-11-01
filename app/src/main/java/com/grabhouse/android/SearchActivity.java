package com.grabhouse.android;

import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.grabhouse.android.com.grabhouse.android.utils.CustomBaseAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SearchActivity extends ActionBarActivity {
    ArrayList<String> title_array = new ArrayList<String>();
    ArrayList<String> notice_array = new ArrayList<String>();
    ArrayList<String> image_url = new ArrayList<String>();
    ListView list;
    CustomBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_in_places);
        list = (ListView) findViewById(R.id.listInPlaces);
        new TheTask().execute();
//        Button rankBtn = (Button) findViewById(R.id.rank_button);
//        rankBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//               final Dialog rankDialog = new Dialog(SearchActivity.this, R.style.FullHeightDialog);
//                rankDialog.setContentView(R.layout.rank_dialog);
//                rankDialog.setCancelable(true);
//                RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
//                ratingBar.setRating(4.0f);
//
//                TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
//                text.setText("Wassup");
//
//                Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
//                updateButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        rankDialog.dismiss();
//                    }
//                });
//                //now that the dialog is set up, it's time to show it
//                rankDialog.show();
//            }
//        });
//        BlurLayout.setGlobalDefaultDuration(450);
//        mSampleLayout = (BlurLayout)findViewById(R.id.blur_layout);
//        View hover = LayoutInflater.from(this).inflate(R.layout.hover_sample, null);
//        hover.findViewById(R.id.heart).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                YoYo.with(Techniques.Tada)
//                        .duration(550)
//                        .playOn(v);
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class TheTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String str = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet("http://128.199.157.171:5555/get_places");
                HttpResponse response = httpclient.execute(httppost);
                str = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return str;

        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String response = result.toString();
            Log.d("response", response);
            try {
                JSONArray new_array = new JSONArray(response);
                for (int i = 0, count = new_array.length(); i < count; i++) {
                    try {
                        JSONObject jsonObject = new_array.getJSONObject(i);
                        title_array.add(jsonObject.getString("name").toString());
                        notice_array.add(jsonObject.getString("listings").toString());
                        image_url.add(jsonObject.getString("image").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new CustomBaseAdapter(SearchActivity.this, title_array, notice_array,image_url);
                list.setAdapter(adapter);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // tv.setText("error2");
            }

        }
    }

}
