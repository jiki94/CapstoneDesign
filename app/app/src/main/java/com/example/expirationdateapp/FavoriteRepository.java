package com.example.expirationdateapp;

import androidx.lifecycle.LiveData;

import java.util.List;

class FavoriteRepository {
    private final AppRoomDatabase database;
    private final FavoriteDao favoriteDao;
    private LiveData<List<Favorite>> favorites;

    FavoriteRepository(AppRoomDatabase database) {
        this.database = database;
        this.favoriteDao = database.favoriteDao();
        favorites = this.favoriteDao.getFavorites();
    }

    LiveData<List<Favorite>> getFavorites(){
        return favorites;
    }

    void insertFavorite(final Favorite newRecord){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                favoriteDao.insertFavorite(newRecord);
            }
        });
    }

    void deleteByName(final String name){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                favoriteDao.deleteByName(name);
            }
        });
    }

    void updateFavorite(final Favorite updated){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                favoriteDao.updateFavorite(updated);
            }
        });
    }
}
