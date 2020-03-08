package com.example.expirationdateapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Favorite.class, BasketItem.class}, version =  1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase{
    public abstract FavoriteDao favoriteDao();
    public abstract BasketItemDao basketItemDao();

    private static final int NUMBER_OF_THREADS = 4;
    final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
}
