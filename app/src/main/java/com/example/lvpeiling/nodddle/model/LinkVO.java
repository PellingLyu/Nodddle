package com.example.lvpeiling.nodddle.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lvpeiling on 2017/5/3.
 */
public class LinkVO implements Parcelable{
    public LinkVO(){

    }
    private String web;
    private String twitter;

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(web);
        dest.writeString(twitter);
    }
    public LinkVO(Parcel in){
        web = in.readString();
        twitter = in.readString();
    }
    public static final Parcelable.Creator<LinkVO> CREATOR = new Creator<LinkVO>() {
        @Override
        public LinkVO createFromParcel(Parcel in) {
            return new LinkVO(in);
        }

        @Override
        public LinkVO[] newArray(int size) {
            return new LinkVO[size];
        }
    };
}
