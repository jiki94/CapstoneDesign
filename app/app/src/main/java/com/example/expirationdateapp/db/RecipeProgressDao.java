package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeProgressDao {
    @Query("SELECT * FROM RecipeProgress WHERE recipeCode = :recipeCode ORDER BY recipeOrder ASC")
    LiveData<List<RecipeProgress>> getRecipeProgress(int recipeCode);
}
