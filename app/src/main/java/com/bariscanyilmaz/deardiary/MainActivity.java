package com.bariscanyilmaz.deardiary;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bariscanyilmaz.deardiary.adapter.PostListAdapter;
import com.bariscanyilmaz.deardiary.data.DiaryDatabase;
import com.bariscanyilmaz.deardiary.data.PostDao;
import com.bariscanyilmaz.deardiary.data.PostViewModel;
import com.bariscanyilmaz.deardiary.databinding.ActivityMainBinding;
import com.bariscanyilmaz.deardiary.di.HashService;
import com.bariscanyilmaz.deardiary.model.Post;
import com.bariscanyilmaz.deardiary.util.App;
import com.bariscanyilmaz.deardiary.util.OnItemClickListener;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private List<Post> posts;
    private RecyclerView postsRecyclerView;
    private ActivityMainBinding binding;
    private FloatingActionButton fab;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PostListAdapter dataAdapter;


    @Inject
    HashService hashService;
    DiaryDatabase db;
    PostDao placeDao;
    PostViewModel mPostViewModel;

    ActivityResultLauncher<Intent> createPostLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {

                if (result.getResultCode() == RESULT_OK) {

                    Log.v("Data","Received");

                    Intent intent=result.getData();
                    if(intent!=null){
                        Post post= intent.getParcelableExtra("post");
                        boolean isEdit=intent.getBooleanExtra("isEdit",false);

                        if (isEdit){

                            mPostViewModel.udpate(post);

                        }else{

                            mPostViewModel.insert(post);
                            posts.add(post);
                            dataAdapter.setData(posts);
                            postsRecyclerView.setAdapter(dataAdapter);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postsRecyclerView=binding.postsRecyclerView;
        fab=binding.btnAddPost;

        fab.setOnClickListener(createPost);

        posts=new ArrayList<>();
        posts.add(new Post("Hello","Hello world",null,null,R.drawable.angry,"Paris",new Date(),this.hashService.hashPassword("123")));
        posts.add(new Post("Good","Hello world",null,null,R.drawable.sad,"Rome",new Date(),this.hashService.hashPassword("123")));
        posts.add(new Post("Party","Hello world",null,null,R.drawable.party,"New York",new Date(),this.hashService.hashPassword("123")));

        dataAdapter=new PostListAdapter(posts,onPostItemClickListener);
        postsRecyclerView.setAdapter(dataAdapter);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        /*
        db = Room.databaseBuilder(getApplicationContext(),
                DiaryDatabase.class, "Diary").allowMainThreadQueries().build();
           */
        App.setActivity(this);

        mPostViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        mPostViewModel.getAllPosts().observe(this, posts -> {
            // Update the cached copy of the words in the adapter.
            dataAdapter.setData(posts);
            postsRecyclerView.setAdapter(dataAdapter);
        });

        //db=DiaryDatabase.getDatabase(getApplicationContext());

        //placeDao = db.postDao();

        //dataAdapter.setData(placeDao.getAll());

        /*
        posts.add(new Post("Hello","Hello world",null,null,R.drawable.angry,"Paris",new Date(),this.hashService.hashPassword("123")));
        posts.add(new Post("Good","Hello world",null,null,R.drawable.sad,"Rome",new Date(),this.hashService.hashPassword("123")));
        posts.add(new Post("Party","Hello world",null,null,R.drawable.party,"New York",new Date(),this.hashService.hashPassword("123")));
           */



    }

    private void handleResponse(List<Post> postList) {
        dataAdapter.setData(postList);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    OnItemClickListener<Post> onPostItemClickListener=new OnItemClickListener<Post>() {
        @Override
        public void onItemClick(Post data) {

            //Alert dialog and check password

            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Enter Password");

            final EditText input=new EditText(MainActivity.this);
            input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
            input.setTransformationMethod(PasswordTransformationMethod.getInstance());

            builder.setView(input);

            builder.setPositiveButton("Show", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String password=input.getText().toString();
                    String hash=MainActivity.this.hashService.hashPassword(password);
                    String generalHash=MainActivity.this.hashService.hashPassword("GENERAL_PASSWORD");

                    if(data.checkPassword(hash)||hash.equals(generalHash)){

                        Intent detail=new Intent(getApplicationContext(),DetailPostActivity.class);

                        detail.putExtra("post",data);

                        startActivity(detail);

                    }else{
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



    View.OnClickListener createPost=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getApplicationContext(),CreatePostActivity.class);
            createPostLauncher.launch(intent);
            //startActivity(intent);
        }
    };

}

