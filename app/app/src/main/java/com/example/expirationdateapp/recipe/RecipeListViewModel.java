package com.example.expirationdateapp.recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.expirationdateapp.db.RecipeInfo;
import com.example.expirationdateapp.db.RecipeInfoRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
    private RecipeInfoRepository recipeInfoRepository;
    private LiveData<List<RecipeInfo>> topFive;

    public RecipeListViewModel(RecipeInfoRepository recipeInfoRepository){
        this.recipeInfoRepository = recipeInfoRepository;
        topFive = recipeInfoRepository.getTopFive();
    }

    LiveData<List<RecipeInfo>> getTopFive(){
        return topFive;
    }
}
