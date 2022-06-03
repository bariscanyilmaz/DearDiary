package com.bariscanyilmaz.deardiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bariscanyilmaz.deardiary.databinding.ActivityDetailPostBinding;
import com.bariscanyilmaz.deardiary.model.Post;

public class DetailPostActivity extends AppCompatActivity {

    ActivityDetailPostBinding binding;
    private Intent intent;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailPostBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        intent=getIntent();
        if(intent!=null){
            post=intent.getParcelableExtra("post");
            binding.detailPostTitle.setText(post.title);
        }

    }
}