package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeIngredientDao {
    @Query("SELECT * FROM RecipeIngredient WHERE recipeCode = :recipeCode AND ingredientType = :type")
    LiveData<List<RecipeIngredient>> getRecipeIngredientsByType(int recipeCode, int type);

    @Query("SELECT DISTINCT ingredientName FROM RecipeIngredient")
    List<String> getDistinctNames();

    @Query("SELECT * FROM RecipeIngredient WHERE recipeCode NOT IN (:disliked) ORDER BY recipeCode")
    LiveData<List<RecipeIngredient>> getIngredientsExclude(List<Integer> disliked);
}
