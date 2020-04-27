package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeInfoRepository {
    private final AppRoomDatabase database;
    private final RecipeInfoDao recipeInfoDao;
    private LiveData<List<RecipeInfo>> topFive;

    public RecipeInfoRepository(AppRoomDatabase database) {
        this.database = database;
        this.recipeInfoDao = database.recipeInfoDao();
        this.topFive = recipeInfoDao.getFiveRecipeInfo();
    }

    public LiveData<List<RecipeInfo>> getTopFive(){
        return topFive;
    }
}
