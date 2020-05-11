package com.example.expirationdateapp.recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.expirationdateapp.db.RecipeInfo;
import com.example.expirationdateapp.db.RecipeInfoRepository;

import java.util.List;

public class DislikedListViewModel extends ViewModel {
    private RecipeInfoRepository recipeInfoRepository;

    public DislikedListViewModel(RecipeInfoRepository recipeInfoRepository) {
        this.recipeInfoRepository = recipeInfoRepository;
    }

    LiveData<List<RecipeInfo>> getDislikedRecipes(){
        return recipeInfoRepository.getDislikedRecipe();
    }
}
