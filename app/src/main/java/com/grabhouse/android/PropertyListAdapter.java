package com.grabhouse.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.grabhouse.android.Models.Property;

import java.util.ArrayList;

/**
 * Created by Dell 3450 on 10/31/2015.
 */
public class PropertyListAdapter extends BaseAdapter {
    Activity mActivity;
    ArrayList<Property> mProperties;
    LayoutInflater inflater;
    public PropertyListAdapter(Activity activity, ArrayList<Property> properties) {
        this.mActivity = activity;
        this.mProperties = properties;
        if (inflater == null)
            if (mActivity != null) {
                inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            }
    }
    @Override
    public int getCount() {
        return mProperties.size();
    }

    @Override
    public Object getItem(int position) {
        return mProperties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_property_row, null);
        TextView price, add, type;
        type = (TextView) view.findViewById(R.id.typeText);
        type.setText(mProperties.get(position).mType);
        price = (TextView) view.findViewById(R.id.priceText);
        price.setText(mProperties.get(position).mValue);
        add = (TextView) view.findViewById(R.id.addText);
        add.setText(mProperties.get(position).mName);
        view.setClickable(true);
        ImageView image = (ImageView) view.findViewById(R.id.propertyImage);
        Glide.with(mActivity).load(mProperties.get(position).mImages[0]).into(image);
        return view;
    }
}
