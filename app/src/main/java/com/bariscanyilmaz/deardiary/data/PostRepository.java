package com.bariscanyilmaz.deardiary.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bariscanyilmaz.deardiary.model.Post;

import java.util.List;

public class PostRepository {

    private PostDao postDao;
    private LiveData<List<Post>> posts;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    PostRepository(Application application) {
        DiaryDatabase db = DiaryDatabase.getDatabase(application);
        postDao = db.postDao();
        posts = postDao.getAll();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Post>> getAllPosts() {
        return posts;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Post post) {
        DiaryDatabase.databaseWriteExecutor.execute(() -> {
            postDao.insert(post);
        });
    }

    void update(Post post) {
        DiaryDatabase.databaseWriteExecutor.execute(() -> {
            postDao.update(post);
        });
    }

    void delete(Post post) {
        DiaryDatabase.databaseWriteExecutor.execute(() -> {
            postDao.delete(post);
        });
    }

}
