package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao{
    // 일단은 Abort 인게 나중에 입력창에서 중복 방지 추가 예정
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertFavorite(Favorite newRecord);

    @Query("SELECT * FROM Favorite")
    LiveData<List<Favorite>> getFavorites();

    @Query("DELETE FROM Favorite WHERE name = :name")
    void deleteByName(String name);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(Favorite updated);
}
