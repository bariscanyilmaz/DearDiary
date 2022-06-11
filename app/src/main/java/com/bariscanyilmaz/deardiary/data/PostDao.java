package com.bariscanyilmaz.deardiary.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bariscanyilmaz.deardiary.model.Post;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PostDao {

    @Query("SELECT * FROM posts")
    LiveData<List<Post>> getAll();

    @Query("SELECT * FROM posts WHERE id=:id")
    Post getById(int id);

    @Update
    void update(Post post);

    @Insert
    void insert(Post post);

    @Delete
    void delete(Post post);




}
