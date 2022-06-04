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
import com.bariscanyilmaz.deardiary.util.OnItemClickListener;

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
        posts.add(new Post("Hello","Lorem Ipsum is simply dummy text of the printing and typesetting industry.\n" +
                "        Lorem Ipsum has been the industry\\'s standard dummy text ever since the 1500s,\n" +
                "        when an unknown printer took a galley of type and scrambled it to make a type specimen book.\n" +
                "        It has survived not only five centuries, but also the leap into electronic typesetting,\n" +
                "        remaining essentially unchanged.\n" +
                "        It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages,\n" +
                "        and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",null,null,R.drawable.angry,"Paris",new Date()));
        posts.add(new Post("Good","Lorem Ipsum is simply dummy text of the printing and typesetting industry.\n" +
                "        Lorem Ipsum has been the industry\\'s standard dummy text ever since the 1500s,\n" +
                "        when an unknown printer took a galley of type and scrambled it to make a type specimen book.\n" +
                "        It has survived not only five centuries, but also the leap into electronic typesetting,\n" +
                "        remaining essentially unchanged.\n" +
                "        It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages,\n" +
                "        and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",null,null,R.drawable.sad,"Rome",new Date()));
        posts.add(new Post("Party","Lorem Ipsum is simply dummy text of the printing and typesetting industry.\n" +
                "        Lorem Ipsum has been the industry\\'s standard dummy text ever since the 1500s,\n" +
                "        when an unknown printer took a galley of type and scrambled it to make a type specimen book.\n" +
                "        It has survived not only five centuries, but also the leap into electronic typesetting,\n" +
                "        remaining essentially unchanged.\n" +
                "        It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages,\n" +
                "        and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",null,null,R.drawable.party,"New York",new Date()));

        postsRecyclerView=binding.postsRecyclerView;
        postsRecyclerView.setAdapter(new PostListAdapter(posts,onPostItemClickListener));
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    OnItemClickListener<Post> onPostItemClickListener=new OnItemClickListener<Post>() {
        @Override
        public void onItemClick(Post data) {
            Intent detail=new Intent(getApplicationContext(),DetailPostActivity.class);
            detail.putExtra("post",data);
            startActivity(detail);
        }
    };



    public void createPost(){
        Log.v("Click","Floating action button clicked");
    }



}