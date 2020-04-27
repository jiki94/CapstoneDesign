package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeInfoDao {
    @Query("SELECT * FROM RecipeInfo LIMIT 5")
    LiveData<List<RecipeInfo>> getFiveRecipeInfo();
}
