package com.bariscanyilmaz.deardiary.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bariscanyilmaz.deardiary.model.Post;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private PostRepository mRepository;

    private final LiveData<List<Post>> mAllPosts;

    public PostViewModel(Application application) {
        super(application);
        mRepository = new PostRepository(application);
        mAllPosts = mRepository.getAllPosts();
    }

    public LiveData<List<Post>> getAllPosts() {
        return mAllPosts;
    }

    public void insert(Post post) {mRepository.insert(post);}
    public void udpate(Post post){mRepository.update(post);}
    public void delete(Post post){mRepository.delete(post);}
}
