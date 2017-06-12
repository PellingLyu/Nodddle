package com.example.lvpeiling.nodddle.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lvpeiling on 2017/5/23.
 */
public class BucketVO implements Parcelable{
    private int id;
    private String name;
    private String description;
    private int shots_count;
    private String created_at;
    private String updated_at;
    private UserVO user;

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getShots_count() {
        return shots_count;
    }

    public void setShots_count(int shots_count) {
        this.shots_count = shots_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(shots_count);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeParcelable(user,flags);
    }
    public BucketVO(){

    }
    public BucketVO(Parcel in){
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        shots_count = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
        user = in.readParcelable(UserVO.class.getClassLoader());
    }
    public static final Parcelable.Creator<BucketVO> CREATOR = new Creator<BucketVO>() {

        @Override
        public BucketVO createFromParcel(Parcel in) {
            return new BucketVO(in);
        }

        @Override
        public BucketVO[] newArray(int size) {
            return new BucketVO[size];
        }
    };
}
