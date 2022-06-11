package com.bariscanyilmaz.deardiary;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.bariscanyilmaz.deardiary.data.PostViewModel;
import com.bariscanyilmaz.deardiary.databinding.ActivityDetailPostBinding;
import com.bariscanyilmaz.deardiary.model.Post;
import com.bariscanyilmaz.deardiary.util.App;
import com.google.android.material.snackbar.Snackbar;

import java.net.URI;
import java.text.DateFormat;

public class DetailPostActivity extends AppCompatActivity {

    ActivityDetailPostBinding binding;
    private Intent intent;
    private Post post;
    PostViewModel postViewModel;

    ActivityResultLauncher<Intent> editPostLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {

                if (result.getResultCode() == RESULT_OK) {

                    Intent intent=result.getData();
                    if(intent!=null){
                        Post post= intent.getParcelableExtra("post");
                        boolean isEdit=intent.getBooleanExtra("isEdit",false);
                        if (isEdit){
                            postViewModel.udpate(post);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        postViewModel = new ViewModelProvider(App.getActivity()).get(PostViewModel.class);
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
                //Bitmap bmImg = BitmapFactory.decodeFile(post.imgPath);
                Uri uri= Uri.parse(post.imgPath);
                binding.postDetailImage.setImageURI(uri);
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
                editPostLauncher.launch(intent);

                //startActivity(intent);
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

                        postViewModel.delete(post);

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