package com.grabhouse.android.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dell 3450 on 10/31/2015.
 */
public class Property implements Parcelable {
    public String mName;
    public String mDesc;
    public String mType;
    public String mLocation;
    public String mValue;
    public String mLat;
    public String mLng;
    public String[] mImages={""};
    public boolean mHasGym;
    public boolean mHasPool;
    public String mVideoID;

    public Property() {

    }

    protected Property(Parcel in) {
        mName = in.readString();
        mDesc = in.readString();
        mType = in.readString();
        mLocation = in.readString();
        mValue = in.readString();
        mLat = in.readString();
        mLng = in.readString();
        mHasGym = in.readByte() != 0x00;
        mHasPool = in.readByte() != 0x00;
        mVideoID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mDesc);
        dest.writeString(mType);
        dest.writeString(mLocation);
        dest.writeString(mValue);
        dest.writeString(mLat);
        dest.writeString(mLng);
        dest.writeByte((byte) (mHasGym ? 0x01 : 0x00));
        dest.writeByte((byte) (mHasPool ? 0x01 : 0x00));
        dest.writeString(mVideoID);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Property> CREATOR = new Parcelable.Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };
}
