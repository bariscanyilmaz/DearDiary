package com.bariscanyilmaz.deardiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bariscanyilmaz.deardiary.databinding.ActivityDetailPostBinding;
import com.bariscanyilmaz.deardiary.model.Post;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;

public class DetailPostActivity extends AppCompatActivity {

    ActivityDetailPostBinding binding;
    private Intent intent;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailPostBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent=getIntent();
        if(intent!=null){

            post=intent.getParcelableExtra("post");

            binding.postDetailDateText.setText(DateFormat.getDateInstance().format(post.date));
            binding.postDetailLocationText.setText(post.location);
            binding.postDetailMoodImage.setImageResource(post.mood);
            binding.postDetailTitleText.setText(post.title);
            binding.postDetailText.setText(post.text);

            if(post.imgPath!=null){
                Bitmap bmImg = BitmapFactory.decodeFile(post.imgPath);
                binding.postDetailImage.setImageBitmap(bmImg);
                binding.postDetailImage.setVisibility(View.VISIBLE);
            }




        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_post_detail,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.postDetailEdit:
                //create
                Intent intent=new Intent(getApplicationContext(),CreatePostActivity.class);
                intent.putExtra("post",post);
                startActivity(intent);
                return true;
            case R.id.postDetailShare:
                //share
                return  true;
            case R.id.postDetailDelete:
                //delete
                //
                AlertDialog.Builder builder=new AlertDialog.Builder(DetailPostActivity.this);
                builder.setTitle("Delete Diary");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Snackbar sb= Snackbar.make(
                                binding.postDetailLayout,
                                "Deleted Diary",
                                Snackbar.LENGTH_SHORT
                        );

                        sb.addCallback(new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                if (event==Snackbar.Callback.DISMISS_EVENT_TIMEOUT){
                                    finish();
                                }
                            }
                        });

                        sb.show();
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

                return true;
            case R.id.postDetailExPortAsPDF:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}