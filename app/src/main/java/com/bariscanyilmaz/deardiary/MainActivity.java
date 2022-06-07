package com.bariscanyilmaz.deardiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bariscanyilmaz.deardiary.adapter.PostListAdapter;
import com.bariscanyilmaz.deardiary.databinding.ActivityMainBinding;
import com.bariscanyilmaz.deardiary.di.HashService;
import com.bariscanyilmaz.deardiary.model.Post;
import com.bariscanyilmaz.deardiary.util.OnItemClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private List<Post> posts;
    private RecyclerView postsRecyclerView;
    private ActivityMainBinding binding;

    @Inject
    HashService hashService;

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
        posts.add(new Post("Hello","Hello world",null,null,R.drawable.angry,"Paris",new Date(),this.hashService.hashPassword("123")));
        posts.add(new Post("Good","Hello world",null,null,R.drawable.sad,"Rome",new Date(),this.hashService.hashPassword("123")));
        posts.add(new Post("Party","Hello world",null,null,R.drawable.party,"New York",new Date(),this.hashService.hashPassword("123")));

        postsRecyclerView=binding.postsRecyclerView;
        postsRecyclerView.setAdapter(new PostListAdapter(posts,onPostItemClickListener));
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    OnItemClickListener<Post> onPostItemClickListener=new OnItemClickListener<Post>() {
        @Override
        public void onItemClick(Post data) {

            //Alert dialog and check password

            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Enter Password");

            final EditText input=new EditText(MainActivity.this);
            input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);

            builder.setView(input);

            builder.setPositiveButton("Show", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String password=input.getText().toString();
                    Log.v("Input Password",password);
                    String hash=MainActivity.this.hashService.hashPassword(password);
                    String generalHash=MainActivity.this.hashService.hashPassword("GENERAL_PASSWORD");

                    if(data.checkPassword(hash)||hash.equals(generalHash)){

                        Intent detail=new Intent(getApplicationContext(),DetailPostActivity.class);

                        detail.putExtra("post",data);

                        startActivity(detail);

                    }else{
                        //

                        Snackbar.make(
                                binding.postsRecyclerView,
                                "Wrong Password",
                                Snackbar.LENGTH_SHORT
                        ).show();
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create();

            builder.show();

        }
    };



    public void createPost(){
        Log.v("Click","Floating action button clicked");
    }



}