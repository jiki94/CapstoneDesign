package com.example.expirationdateapp;

import android.content.Context;

import androidx.room.Room;

public class AppContainer {
    AppRoomDatabase database;
    FavoriteDao favoriteDao;
    FavoriteRepository favoriteRepository;

    public AppContainer(Context context){
        database = Room.databaseBuilder(context.getApplicationContext(), AppRoomDatabase.class, "app_room_database").build();
        favoriteDao = database.favoriteDao();
        favoriteRepository = new FavoriteRepository(database);
    }
}
