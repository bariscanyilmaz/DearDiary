package com.bariscanyilmaz.deardiary.model;

import java.util.Date;

public class Post {
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

}
