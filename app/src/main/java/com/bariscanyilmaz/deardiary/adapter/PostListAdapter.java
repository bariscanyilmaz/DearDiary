package com.bariscanyilmaz.deardiary.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bariscanyilmaz.deardiary.DetailPostActivity;
import com.bariscanyilmaz.deardiary.R;
import com.bariscanyilmaz.deardiary.databinding.PostListItemBinding;
import com.bariscanyilmaz.deardiary.model.Post;
import com.bariscanyilmaz.deardiary.util.OnItemClickListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {


    private List<Post> posts;
    private PostListItemBinding binding;
    private OnItemClickListener<Post> onItemClickListener;
    public PostListAdapter(List<Post> posts,OnItemClickListener<Post> onItemClickListener){
        this.posts=posts;
        this.onItemClickListener=onItemClickListener;
    }
    public void setData(List<Post> data){
        this.posts=data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding=PostListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=posts.get(position);

        holder.binding.moodImgView.setImageResource(post.mood);
        holder.binding.dateTxtView.setText(DateFormat.getDateInstance().format(post.date));
        holder.binding.titleTxtView.setText(post.title);
        holder.binding.locationTxtView.setText(post.location);

        holder.binding.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //TODO confirm password
                PostListAdapter.this.onItemClickListener.onItemClick(post);
            }
        });


    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private PostListItemBinding binding;

        public ViewHolder(@NonNull PostListItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}


