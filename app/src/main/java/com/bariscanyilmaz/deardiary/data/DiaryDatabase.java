package com.bariscanyilmaz.deardiary.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.bariscanyilmaz.deardiary.model.Post;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Post.class},version = 1)
@TypeConverters({Converters.class})
public abstract class DiaryDatabase extends RoomDatabase {

    public abstract PostDao postDao();


    private static volatile DiaryDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static DiaryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DiaryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DiaryDatabase.class, "Diary.db").allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
