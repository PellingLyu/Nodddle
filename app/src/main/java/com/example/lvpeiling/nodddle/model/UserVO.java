package com.example.lvpeiling.nodddle.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lvpeiling on 2017/5/3.
 */
public class UserVO implements Parcelable{

    public UserVO(){

    }
    private int id;
    private String name;
    private String username;
    private String html_url;
    private String avatar_url;
    private String bio;
    private String location;
    private LinkVO links;
    private int buckets_count;
    private int comments_received_count;
    private int followers_count;
    private int followings_count;
    private int likes_count;
    private int likes_received_count;
    private int projects_count;
    private int rebounds_received_count;
    private int shots_count;
    private int teams_count;
    private boolean can_upload_shot;
    private String type;
    private boolean pro;
    private String buckets_url;
    private String followers_url;
    private String following_url;
    private String likes_url;
    private String projects_url;
    private String shots_url;
    private String teams_url;
    private String created_at;
    private String updated_at;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LinkVO getLinks() {
        return links;
    }

    public void setLinks(LinkVO links) {
        this.links = links;
    }

    public int getBuckets_count() {
        return buckets_count;
    }

    public void setBuckets_count(int buckets_count) {
        this.buckets_count = buckets_count;
    }

    public int getComments_received_count() {
        return comments_received_count;
    }

    public void setComments_received_count(int comments_received_count) {
        this.comments_received_count = comments_received_count;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFollowings_count() {
        return followings_count;
    }

    public void setFollowings_count(int followings_count) {
        this.followings_count = followings_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getLikes_received_count() {
        return likes_received_count;
    }

    public void setLikes_received_count(int likes_received_count) {
        this.likes_received_count = likes_received_count;
    }

    public int getProjects_count() {
        return projects_count;
    }

    public void setProjects_count(int projects_count) {
        this.projects_count = projects_count;
    }

    public int getRebounds_received_count() {
        return rebounds_received_count;
    }

    public void setRebounds_received_count(int rebounds_received_count) {
        this.rebounds_received_count = rebounds_received_count;
    }

    public int getShots_count() {
        return shots_count;
    }

    public void setShots_count(int shots_count) {
        this.shots_count = shots_count;
    }

    public int getTeams_count() {
        return teams_count;
    }

    public void setTeams_count(int teams_count) {
        this.teams_count = teams_count;
    }

    public boolean isCan_upload_shot() {
        return can_upload_shot;
    }

    public void setCan_upload_shot(boolean can_upload_shot) {
        this.can_upload_shot = can_upload_shot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPro() {
        return pro;
    }

    public void setPro(boolean pro) {
        this.pro = pro;
    }

    public String getBuckets_url() {
        return buckets_url;
    }

    public void setBuckets_url(String buckets_url) {
        this.buckets_url = buckets_url;
    }

    public String getFollowers_url() {
        return followers_url;
    }

    public void setFollowers_url(String followers_url) {
        this.followers_url = followers_url;
    }

    public String getFollowing_url() {
        return following_url;
    }

    public void setFollowing_url(String following_url) {
        this.following_url = following_url;
    }

    public String getLikes_url() {
        return likes_url;
    }

    public void setLikes_url(String likes_url) {
        this.likes_url = likes_url;
    }

    public String getProjects_url() {
        return projects_url;
    }

    public void setProjects_url(String projects_url) {
        this.projects_url = projects_url;
    }

    public String getShots_url() {
        return shots_url;
    }

    public void setShots_url(String shots_url) {
        this.shots_url = shots_url;
    }

    public String getTeams_url() {
        return teams_url;
    }

    public void setTeams_url(String teams_url) {
        this.teams_url = teams_url;
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
        dest.writeString(username);
        dest.writeString(html_url);
        dest.writeString(avatar_url);
        dest.writeString(bio);
        dest.writeString(location);
        dest.writeParcelable(links,flags);
        dest.writeInt(buckets_count);
        dest.writeInt(comments_received_count);
        dest.writeInt(followers_count);
        dest.writeInt(followings_count);
        dest.writeInt(likes_count);
        dest.writeInt(likes_received_count);
        dest.writeInt(projects_count);
        dest.writeInt(rebounds_received_count);
        dest.writeInt(shots_count);
        dest.writeInt(teams_count);
        dest.writeByte((byte) (can_upload_shot?1:0));
        dest.writeString(type);
        dest.writeByte((byte) (pro?1:0));
        dest.writeString(buckets_url);
        dest.writeString(followers_url);
        dest.writeString(following_url);
        dest.writeString(likes_url);
        dest.writeString(projects_url);
        dest.writeString(shots_url);
        dest.writeString(teams_url);
        dest.writeString(created_at);
        dest.writeString(updated_at);

    }

    public UserVO(Parcel in){
        id = in.readInt();
        name = in.readString();
        username = in.readString();
        html_url = in.readString();
        avatar_url = in.readString();
        bio = in.readString();
        location = in.readString();
        links = in.readParcelable(LinkVO.class.getClassLoader());
        buckets_count = in.readInt();
        comments_received_count = in.readInt();
        followers_count = in.readInt();
        followings_count = in.readInt();
        likes_count = in.readInt();
        likes_received_count = in.readInt();
        projects_count = in.readInt();
        rebounds_received_count = in.readInt();
        shots_count = in.readInt();
        teams_count = in.readInt();
        can_upload_shot = in.readByte() != 0;
        type = in.readString();
        pro = in.readByte() != 0;
        buckets_url = in.readString();
        followers_url = in.readString();
        following_url = in.readString();
        likes_url = in.readString();
        projects_url = in.readString();
        shots_url = in.readString();
        teams_url = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Parcelable.Creator<UserVO> CREATOR = new Creator<UserVO>() {

        @Override
        public UserVO createFromParcel(Parcel in) {
            return new UserVO(in);
        }

        @Override
        public UserVO[] newArray(int size) {
            return new UserVO[size];
        }
    };
}