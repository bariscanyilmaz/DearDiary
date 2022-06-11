package com.bariscanyilmaz.deardiary.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bariscanyilmaz.deardiary.di.HashService;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;

import javax.inject.Inject;

@Entity(tableName = "posts")
public class Post implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "text")
    public String text;
    @ColumnInfo(name = "imgPath")
    public String imgPath;
    @ColumnInfo(name = "videoPath")
    public String videoPath;
    @ColumnInfo(name = "mood")
    public int mood;
    @ColumnInfo(name = "location")
    public String location;
    @ColumnInfo(name = "date")
    public Date date;
    @ColumnInfo(name = "password")
    public String password;
    @ColumnInfo(name = "latitude")
    public double latitude;
    @ColumnInfo(name = "longitude")
    public double longitude;

    public Post(){

    }

    public Post(String title,String text,String imgPath,String videoPath,int mood,String location,Date date,String password){
        this.title=title;
        this.text=text;
        this.imgPath=imgPath;
        this.videoPath=videoPath;
        this.mood=mood;
        this.location=location;
        this.date=date;
        this.password=password;
    }

    public boolean checkPassword(String hash){
        return  this.password.equals(hash);
    }

    protected Post(Parcel parcel) {
        this.id=parcel.readInt();
        this.title=parcel.readString();
        this.text=parcel.readString();
        this.imgPath= parcel.readString();
        this.videoPath= parcel.readString();
        this.mood=parcel.readInt();
        this.location=parcel.readString();
        this.date=new Date(parcel.readLong());
        this.password=parcel.readString();
        this.latitude=parcel.readDouble();
        this.longitude=parcel.readDouble();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.text);
        parcel.writeString(this.imgPath);
        parcel.writeString(this.videoPath);
        parcel.writeInt(this.mood);
        parcel.writeString(this.location);
        parcel.writeLong(this.date.getTime());
        parcel.writeString(this.password);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);

    }
    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public void setDate(Date date) {
        this.date = date;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
