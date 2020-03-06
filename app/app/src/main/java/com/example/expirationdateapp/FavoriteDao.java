package com.example.expirationdateapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao{
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertFavorite(Favorite newRecord);

    @Query("SELECT * FROM Favorite")
    LiveData<List<Favorite>> getFavorites();

    @Query("DELETE FROM Favorite WHERE name = :name")
    void deleteByName(String name);
}
