package com.grabhouse.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.grabhouse.android.Models.Property;

import java.util.ArrayList;

/**
 * Created by Dell 3450 on 11/1/2015.
 */
public class HorizontalListViewAdapter {
    ArrayList<Property> mProperties;
    Activity mActivity;
    LayoutInflater inflater;
    GestureDetector gestureDetector;
    public HorizontalListViewAdapter(Activity activity, ArrayList<Property> properties) {
        mActivity = activity;
        mProperties = properties;
        if (inflater == null)
            if (mActivity != null) {
                inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            }
    }
    public View getView() {
        LinearLayout linearLayout = new LinearLayout(mActivity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i=0;i<mProperties.size();i++) {
            final RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.layout_property_row, null);
            TextView price, add, type;
            type = (TextView) view.findViewById(R.id.typeText);
            type.setText(mProperties.get(i).mType);
            price = (TextView) view.findViewById(R.id.priceText);
            price.setText(mProperties.get(i).mValue);
            add = (TextView) view.findViewById(R.id.addText);
            add.setText(mProperties.get(i).mName);
            view.setClickable(true);
            view.setTag(""+i);
            view.setLongClickable(true);
            ImageView image = (ImageView) view.findViewById(R.id.propertyImage);
            Glide.with(mActivity).load(mProperties.get(i).mImages[0]).into(image);
            gestureDetector = new GestureDetector(mActivity, new SingleTapUp());
            final int finalI = i;
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                   if (!((MainActivity) mActivity).touchedUp) {
                        ((MainActivity) mActivity).initYoutube();
                        return true;
                    } else
                        return false;
                }


            });
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gestureDetector.onTouchEvent(event)) {
                        Intent i = new Intent(mActivity, PlayActivity.class);
                        Log.d("Tag",  view.getTag().toString());
                        i.putExtra("img", mProperties.get(Integer.parseInt(view.getTag().toString())).mImages[0]);
                        i.putExtra("propObj", mProperties.get(Integer.parseInt(view.getTag().toString())));
                        i.putExtra("latlng", mProperties.get(Integer.parseInt(view.getTag().toString())).mLat + "," + mProperties.get(Integer.parseInt(view.getTag().toString())).mLng);
                        mActivity.startActivity(i);
                        return true;
                    }
                    else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        Log.d("TouchTest", "Touch up");
                        ((MainActivity) mActivity).touchedUp = true;
                        ((MainActivity) mActivity).releaseYT();
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        ((MainActivity) mActivity).touchedUp = false;
                        return false;
                    }
                    return false;
                }
            });
            linearLayout.addView(view);
        }
        return linearLayout;
    }
    private class  SingleTapUp extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

}
