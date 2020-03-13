package com.example.expirationdateapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Favorite.class, Product.class}, version =  1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase{
    public abstract FavoriteDao favoriteDao();
    public abstract ProductDao productDao();

    private static final int NUMBER_OF_THREADS = 4;
    final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
}
