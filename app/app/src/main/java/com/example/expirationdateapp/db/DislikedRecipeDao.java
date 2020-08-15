package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DislikedRecipeDao {
    @Insert
    void insertDisliked(DislikedRecipe dislikedRecipe);

    @Delete
    void deleteDisliked(DislikedRecipe dislikedRecipe);

    @Query("SELECT recipeCode FROM DislikedRecipe")
    LiveData<List<Integer>> getDisliked();
}
