package com.example.blogs.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AppUser implements Parcelable {
    private String userId = "";
    private String email = "";
    private String username = "";
    private String phone = "";
    private String imageUrl = "";

    public AppUser(String userId, String username, String phone, String email, String imageUrl) {
        this.userId = userId;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public AppUser() {
    }

    protected AppUser(Parcel in) {
        userId = in.readString();
        email = in.readString();
        username = in.readString();
        phone = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<AppUser> CREATOR = new Creator<AppUser>() {
        @Override
        public AppUser createFromParcel(Parcel in) {
            return new AppUser(in);
        }

        @Override
        public AppUser[] newArray(int size) {
            return new AppUser[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(phone);
        dest.writeString(imageUrl);
    }
}
