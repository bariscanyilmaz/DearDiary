package com.bariscanyilmaz.deardiary;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;

import android.widget.TextView;

import com.bariscanyilmaz.deardiary.data.DiaryDatabase;
import com.bariscanyilmaz.deardiary.data.PostDao;
import com.bariscanyilmaz.deardiary.data.PostViewModel;
import com.bariscanyilmaz.deardiary.databinding.ActivityCreatePostBinding;
import com.bariscanyilmaz.deardiary.di.HashService;
import com.bariscanyilmaz.deardiary.model.MoodItem;
import com.bariscanyilmaz.deardiary.model.Post;

import com.bariscanyilmaz.deardiary.util.SaveSystem;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class CreatePostActivity extends AppCompatActivity {

    ActivityCreatePostBinding binding;
    private Intent intent;
    private Post post;
    private boolean isEdit;
    private String[] moodList=new String[]{
            "Party",
            "Angry",
            "Confused",
            "Sad",
            "Very Sad",
            "Happy",
            "Tired"
    };
    private int[] moodIcons=new int[]{
            R.drawable.party,
            R.drawable.angry,
            R.drawable.confused,
            R.drawable.sad,
            R.drawable.sad2,
            R.drawable.smile,
            R.drawable.tired
    };
    /**/
    private MoodItem[] moodItems=new MoodItem[]{
            new MoodItem("Party", R.drawable.party),
            new MoodItem("Angry",R.drawable.angry),
            new MoodItem("Confused",R.drawable.confused),
            new MoodItem("Sad",R.drawable.sad),
            new MoodItem("Very Sad",R.drawable.sad2),
            new MoodItem("Smile",R.drawable.smile),
            new MoodItem("Tired",R.drawable.tired)
    };

    private int choosenMood;

    InputMethodManager imm;
    private SharedPreferences sharedPreferences;
    @Inject
    HashService hashService;

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {

                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    // Use the uri to load the image
                    binding.postCreateImage.setImageURI(uri);
                    post.imgPath=(uri.toString());
                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error
                }
            });

    ActivityResultLauncher<Intent> locationLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {

                if (result.getResultCode() == RESULT_OK) {

                    //Uri uri = result.getData().getData();
                    double latitude=result.getData().getDoubleExtra("lat",0);
                    double longitude=result.getData().getDoubleExtra("lng",0);
                    String city= result.getData().getStringExtra("city");

                    post.location=(city);
                    post.latitude=(latitude);
                    post.longitude=(longitude);

                    binding.postCreateLocationText.setText(city);
                }
            });

    PostDao postDao;
    DiaryDatabase db;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    PostViewModel mPostViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences=getApplicationContext().getSharedPreferences("diaries", Context.MODE_PRIVATE);
        //mPostViewModel = new ViewModelProvider(MainActivity.instance).get(PostViewModel.class);
        /*
        db = Room.databaseBuilder(getApplicationContext(),
                DiaryDatabase.class, "Diary").allowMainThreadQueries().build();

        postDao = db.postDao();
        */
        intent=getIntent();
        post=intent.getParcelableExtra("post");
        imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if(post!=null){

                isEdit=true;
                binding.postCreateDateText.setText(DateFormat.getDateInstance().format(post.date));
                binding.postCreateLocationText.setText(post.location);
                binding.postCreateMoodImage.setImageResource(post.mood);
                binding.postCreateTitleText.setText(post.title);
                binding.postCreateText.setText(post.text);

                if(post.imgPath!=null){
                    //Bitmap bmImg = BitmapFactory.decodeFile(post.imgPath);
                    Uri uri=Uri.parse(post.imgPath);
                    binding.postCreateImage.setImageURI(uri);
                    binding.postCreateImage.setVisibility(View.VISIBLE);
                }

        }else{
            post=new Post();
            //
            binding.postCreateDateText.setText(DateFormat.getDateInstance().format(new Date()));
            post.date=new Date();
            post.mood=R.drawable.confused;
            binding.postCreateTitleText.setHint("Title");
            binding.postCreateText.setHint("Content");
            binding.postCreateImage.setVisibility(View.VISIBLE);
        }

        //
        binding.postCreateMoodImage.setOnClickListener(changeMood);
        binding.postCreateDateText.setOnClickListener(changeDate);
        binding.postCreateTitleText.setOnClickListener(changeTitle);
        binding.postCreateImage.setOnClickListener(changeImage);
        binding.postCreateText.setOnClickListener(changeContent);
        binding.postCreateLocationText.setOnClickListener(changeLocation);
        binding.postCreateFocusText.setOnEditorActionListener(focusTextDoneListener);
        binding.postCreateFocusText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.postCreateFocusText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        binding.postCreateFocusText.setOnFocusChangeListener(focusChangeListener);
        binding.createPostSetPasswordBtn.setOnClickListener(changPassword);

    }

    View.OnClickListener changPassword=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this);
            builder.setTitle("New Password");
            // Set up the input
            EditText input = new EditText(CreatePostActivity.this);

            input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
            input.setTransformationMethod(PasswordTransformationMethod.getInstance());

            builder.setView(input);


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //hashService
                    String password=input.getText().toString();
                    Log.v("Password",password);

                    String hash=hashService.hashPassword(password);
                    post.password=(hash);

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create();

            builder.show();
        }
    };
    View.OnFocusChangeListener focusChangeListener=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean isFocus) {
            if (isFocus){

            }else{

                String content=binding.postCreateFocusText.getText().toString();

                binding.postCreateText.setText(content);
                post.text=(content);
                binding.postCreateFocusText.setVisibility(View.GONE);
                binding.postCreateText.setVisibility(View.VISIBLE);

            }
        }
    };
    TextView.OnEditorActionListener focusTextDoneListener= new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if(actionId== EditorInfo.IME_ACTION_DONE){

                String content=binding.postCreateFocusText.getText().toString();
                binding.postCreateText.setText(content);
                post.text=(content);
                binding.postCreateFocusText.setVisibility(View.GONE);
                binding.postCreateText.setVisibility(View.VISIBLE);

                imm.hideSoftInputFromWindow(binding.postCreateFocusText.getWindowToken(), 0);

                return true;
            }
            return false;
        }
    };
    View.OnClickListener changeLocation=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getApplicationContext(),MapsActivity.class);

            if(isEdit){

                intent.putExtra("info","edit");
                intent.putExtra("lat",post.latitude);
                intent.putExtra("lng",post.longitude);

            }else{
                intent.putExtra("info","new");
            }

            locationLauncher.launch(intent);

        }
    };
    View.OnClickListener changeContent=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                //open mutli text line
            binding.postCreateText.setVisibility(View.GONE);
            binding.postCreateFocusText.setVisibility(View.VISIBLE);
            binding.postCreateFocusText.requestFocus();
            if (post.text!=null)
                binding.postCreateFocusText.setText(post.text);

            imm.showSoftInput(binding.postCreateFocusText, InputMethodManager.SHOW_IMPLICIT);

        }
    };
    View.OnClickListener changeImage=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //image picker
            launcher.launch(
                    ImagePicker.with(CreatePostActivity.this)
                            .galleryOnly()	//User can only select image from Gallery
                            .createIntent()
            );
        }
    };
    View.OnClickListener changeMood=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                //show moods in list
            MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(CreatePostActivity.this);
            builder
            .setTitle("Choose your mood")
                    .setPositiveButton("Confirm",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            binding.postCreateMoodImage.setImageResource(choosenMood);
                            post.mood=(choosenMood);
                        }
                    })
                    .setNegativeButton("Cancel",(dialog,i)->{dialog.cancel();})
                    .setSingleChoiceItems(moodList, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            choosenMood=moodIcons[i];


                        }
                    })
                    .show();
        }
    };
    View.OnClickListener changeDate=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            MaterialDatePicker datePicker= MaterialDatePicker.Builder.datePicker()
                    .build();
            datePicker.addOnPositiveButtonClickListener(setDateListener);
            datePicker.show(getSupportFragmentManager(),"tag");


        }
    };

    MaterialPickerOnPositiveButtonClickListener setDateListener=new MaterialPickerOnPositiveButtonClickListener() {
        @Override
        public void onPositiveButtonClick(Object selection) {

            Long date=(Long)selection;
            Date selectedDate=new Date(date);
            post.date=(selectedDate);
            binding.postCreateDateText.setText(DateFormat.getDateInstance().format(selectedDate));
        }
    };

    View.OnClickListener changeTitle=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                //open text field
            final EditText input=new EditText(CreatePostActivity.this);
            AlertDialog.Builder builder=new AlertDialog.Builder(CreatePostActivity.this);
            builder.setTitle("Enter Title");
            builder.setView(input);

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    String title=input.getText().toString();
                    post.title=(title);
                    //Save the title;
                    binding.postCreateTitleText.setText(title);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();


        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_post_create,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

            case R.id.postCreateSave:
                //
                if(!checkPostCanSave(post)) return false;

                if (!isEdit){
                    //TODO insert post
                    //postDao.insert(post);
                    intent.putExtra("isEdit", false);
                    intent.putExtra("post",post);
                    setResult(RESULT_OK,intent);
                    finish();
                    return true;
                }else{

                    intent.putExtra("isEdit", true);
                    intent.putExtra("post",post);
                    setResult(RESULT_OK,intent);
                    finish();
                    return true;
                }
        }

        return super.onOptionsItemSelected(item);
    }
    private void handleResponse(){
        Snackbar.make(binding.postCreateLayout,"Post Saved Successfully",Snackbar.LENGTH_SHORT).addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                finish();
            }
        }).show();

    }

    private boolean checkPostCanSave(Post post){
        if (post.location==null){
            Snackbar.make(binding.postCreateLayout,"Location can not be empty",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (post.text==null){
            Snackbar.make(binding.postCreateLayout,"Content can not be empty",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (post.title==null){
            Snackbar.make(binding.postCreateLayout,"Title can not be empty",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (post.password==null){
            Snackbar.make(binding.postCreateLayout,"Password can not be empty",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}