package com.bariscanyilmaz.deardiary.util;

import android.content.SharedPreferences;

import com.bariscanyilmaz.deardiary.model.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SaveSystem {
    public static void saveDiary(SharedPreferences sharedPreferences, List<Post> postList){

        Gson gson=new Gson();
        Type type=new TypeToken<List<Post>>() {}.getType();
        String json=gson.toJson(postList,type);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("posts",json).apply();
    }

    public static List<Post> getDiaries(SharedPreferences sharedPreferences){
        String playListsJson="";

        playListsJson=sharedPreferences.getString("posts",null);

        if (playListsJson==null) return new ArrayList<>();

        Gson gson=new Gson();

        Type type = new TypeToken<List<Post>>() {}.getType();

        ArrayList<Post> result = gson.fromJson(playListsJson, type);

        return result;
    }

}
