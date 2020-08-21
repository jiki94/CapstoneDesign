package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class RecipeInfoRepository {
    private final AppRoomDatabase database;
    private final RecipeInfoDao recipeInfoDao;

    LiveData<List<RecipeInfoAndAlmost>> recommendedRecipeLiveData;

    public RecipeInfoRepository(AppRoomDatabase database) {
        this.database = database;
        this.recipeInfoDao = database.recipeInfoDao();

        this.recommendedRecipeLiveData = recipeInfoDao.getRecommendRecipes(LocalDate.now(), LocalDate.now().plusDays(3));
    }

    public LiveData<List<RecipeInfoAndAlmost>> getRecommendRecipes(){
        return recommendedRecipeLiveData;
    }


    public LiveData<RecipeInfo> getRecipeInfo(int recipeCode){
        return recipeInfoDao.getRecipeInfo(recipeCode);
    }

    public LiveData<List<RecipeInfoAndAlmost>> getRecipeInfoSearchBy(String searchWord){
        return recipeInfoDao.getRecommendRecipes(searchWord, LocalDate.now(), LocalDate.now().plusDays(3));
    }

    public LiveData<List<RecipeInfo>> getDislikedRecipe(){
        return recipeInfoDao.getDislikedRecipes();
    }
}
