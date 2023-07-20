package com.example.blogs.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Blog implements Parcelable {
    private String userId;
    private String blogId;
    private String imageUrl;
    private String title;
    private String content;

    public Blog() {
    }

    public Blog(String userId, String blogId, String imageUrl, String title, String content) {
        this.userId = userId;
        this.blogId = blogId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.content = content;
    }

    protected Blog(Parcel in) {
        userId = in.readString();
        blogId = in.readString();
        imageUrl = in.readString();
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel in) {
            return new Blog(in);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(blogId);
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(content);
    }

    @Override
    public String toString() {
        return "Blog{" +
                "userId='" + userId + '\'' +
                ", blogId='" + blogId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
