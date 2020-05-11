package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeIngredientRepository {
    private final AppRoomDatabase database;
    private final RecipeIngredientDao recipeIngredientDao;

    public RecipeIngredientRepository(AppRoomDatabase database) {
        this.database = database;
        this.recipeIngredientDao = database.recipeIngredientDao();
    }

    public LiveData<List<RecipeIngredient>> getMainIngredient(int recipeCode) {
        return recipeIngredientDao.getRecipeIngredientsByType(recipeCode, RecipeIngredient.MAIN);
    }

    public LiveData<List<RecipeIngredient>> getSubIngredient(int recipeCode) {
        return recipeIngredientDao.getRecipeIngredientsByType(recipeCode, RecipeIngredient.SUB);
    }

    public LiveData<List<RecipeIngredient>> getSeasoningIngredient(int recipeCode) {
        return recipeIngredientDao.getRecipeIngredientsByType(recipeCode, RecipeIngredient.SEASONING);
    }
}
