package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.Set;

@Dao
public interface MainIngredientCountDao {
    @Query("SELECT recipeCode, count(*) as count FROM RecipeIngredient WHERE ingredientType = 3060001 GROUP BY recipeCode")
    List<MainIngredientCount> calculate();

    @Query("DELETE FROM MainIngredientCount")
    void deleteAll();

    @Insert
    void insert(List<MainIngredientCount> data);
}
