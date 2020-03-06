package com.example.expirationdateapp;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FavoriteRepository {
    private final AppRoomDatabase database;
    private final FavoriteDao favoriteDao;
    private LiveData<List<Favorite>> favorites;

    public FavoriteRepository(AppRoomDatabase database) {
        this.database = database;
        this.favoriteDao = database.favoriteDao();
        favorites = this.favoriteDao.getFavorites();
    }

    public LiveData<List<Favorite>> getFavorites(){
        return favorites;
    }

    public void insertFavorite(final Favorite newRecord){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                favoriteDao.insertFavorite(newRecord);
            }
        });
    }

    public void deleteByName(final String name){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                favoriteDao.deleteByName(name);
            }
        });
    }
}
