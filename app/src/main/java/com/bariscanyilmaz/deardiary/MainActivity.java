package com.bariscanyilmaz.deardiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.bariscanyilmaz.deardiary.adapter.PostListAdapter;
import com.bariscanyilmaz.deardiary.databinding.ActivityMainBinding;
import com.bariscanyilmaz.deardiary.model.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Post> posts;
    private RecyclerView postsRecyclerView;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding= ActivityMainBinding.inflate(getLayoutInflater());

        binding.btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Fab","clicked");
            }
        });
        setContentView(binding.getRoot());
        posts=new ArrayList<>();
        posts.add(new Post("Hello","Hello world",R.drawable.angry,new Date()));
        posts.add(new Post("Good","Hello world",R.drawable.sad,new Date()));
        posts.add(new Post("Party","Hello world",R.drawable.party,new Date()));

        postsRecyclerView=binding.postsRecyclerView;
        postsRecyclerView.setAdapter(new PostListAdapter(posts));
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void createPost(){

        Log.v("Click","Floating action button clicked");

    }



}