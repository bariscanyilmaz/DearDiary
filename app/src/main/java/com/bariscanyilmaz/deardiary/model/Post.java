package com.bariscanyilmaz.deardiary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Post implements Parcelable {
    public int id;
    public String title;
    public String text;
    public String imgPath;
    public String videoPath;
    public int mood;
    public String location;
    public Date date;

    public Post(String title,String text,int mood,Date date){
        this.title=title;
        this.text=text;
        this.mood=mood;
        this.date=date;
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

    }
    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
