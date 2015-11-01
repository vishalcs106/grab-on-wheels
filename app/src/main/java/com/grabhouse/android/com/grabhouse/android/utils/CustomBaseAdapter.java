package com.grabhouse.android.com.grabhouse.android.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.grabhouse.android.R;

import java.util.ArrayList;

/**
 * Created by ckasturi on 11/1/15.
 */
public class CustomBaseAdapter extends BaseAdapter {

    private Activity activity;
    // private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
    private static ArrayList title, notice,imgs;
    private static LayoutInflater inflater = null;

    public CustomBaseAdapter(Activity a, ArrayList b, ArrayList bod,ArrayList img) {
        activity = a;
        this.title = b;
        this.notice = bod;
        this.imgs = img;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return title.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.row_listitem, null);

        TextView title2 = (TextView) vi.findViewById(R.id.txt_ttlsm_row); // title
        String song = title.get(position).toString();
        title2.setText(song);


        TextView title22 = (TextView) vi.findViewById(R.id.txt_ttlcontact_row2); // notice
        String song2 = notice.get(position).toString()+" listings";
        title22.setText(song2);

        ImageView iv = (ImageView) vi.findViewById(R.id.iv_icon_social); // notice
        Glide.with(vi.getContext()).load(imgs.get(position).toString()).into(iv);
        return vi;

    }

}