package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeProgressRepository {
    private final AppRoomDatabase database;
    private final RecipeProgressDao recipeProgressDao;

    public RecipeProgressRepository(AppRoomDatabase database) {
        this.database = database;
        this.recipeProgressDao = database.recipeProgressDao();
    }

    public LiveData<List<RecipeProgress>> getRecipeProgress(int recipeCode) {
        return recipeProgressDao.getRecipeProgress(recipeCode);
    }
}
