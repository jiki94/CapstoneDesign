package com.example.expirationdateapp.recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.expirationdateapp.db.RecipeInfo;
import com.example.expirationdateapp.db.RecipeInfoRepository;

import org.threeten.bp.LocalDate;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
    private RecipeInfoRepository recipeInfoRepository;
    private LiveData<List<RecipeInfo>> recommendRecipes;

    public RecipeListViewModel(RecipeInfoRepository recipeInfoRepository){
        this.recipeInfoRepository = recipeInfoRepository;
        recommendRecipes = recipeInfoRepository.getRecommendRecipes();
    }

    LiveData<List<RecipeInfo>> getRecommendRecipes(){
        return recommendRecipes;
    }
}
