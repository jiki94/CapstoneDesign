package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;

import org.threeten.bp.LocalDate;

import java.util.List;

public class RecipeInfoRepository {
    private final AppRoomDatabase database;
    private final RecipeInfoDao recipeInfoDao;

    public RecipeInfoRepository(AppRoomDatabase database) {
        this.database = database;
        this.recipeInfoDao = database.recipeInfoDao();
    }

    public LiveData<List<RecipeInfo>> getRecommendRecipes(){
        return recipeInfoDao.getRecommendRecipes(LocalDate.now());
    }

    public LiveData<RecipeInfo> getRecipeInfo(int recipeCode){
        return recipeInfoDao.getRecipeInfo(recipeCode);
    }

    public LiveData<List<RecipeInfo>> getRecipeInfoSearchBy(String searchWord){
        return recipeInfoDao.getRecommendRecipes(searchWord);
    }
}
