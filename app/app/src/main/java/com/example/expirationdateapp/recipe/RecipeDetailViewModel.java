package com.example.expirationdateapp.recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.expirationdateapp.db.RecipeInfo;
import com.example.expirationdateapp.db.RecipeInfoRepository;
import com.example.expirationdateapp.db.RecipeIngredient;
import com.example.expirationdateapp.db.RecipeIngredientRepository;
import com.example.expirationdateapp.db.RecipeProgress;
import com.example.expirationdateapp.db.RecipeProgressRepository;

import java.util.List;

public class RecipeDetailViewModel extends ViewModel {
    private RecipeInfoRepository recipeInfoRepository;
    private RecipeIngredientRepository recipeIngredientRepository;
    private RecipeProgressRepository recipeProgressRepository;

    public RecipeDetailViewModel(RecipeInfoRepository recipeInfoRepository, RecipeIngredientRepository recipeIngredientRepository,
                                 RecipeProgressRepository recipeProgressRepository){
        this.recipeInfoRepository = recipeInfoRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeProgressRepository = recipeProgressRepository;
    }

    LiveData<RecipeInfo> getRecipeInfo(int recipeCode){
        return recipeInfoRepository.getRecipeInfo(recipeCode);
    }

    LiveData<List<RecipeIngredient>> getMainIngredient(int recipeCode){
        return recipeIngredientRepository.getMainIngredient(recipeCode);
    }

    LiveData<List<RecipeIngredient>> getSubIngredient(int recipeCode){
        return recipeIngredientRepository.getSubIngredient(recipeCode);
    }

    LiveData<List<RecipeIngredient>> getSeasoningIngredient(int recipeCode){
        return recipeIngredientRepository.getSeasoningIngredient(recipeCode);
    }

    LiveData<List<RecipeProgress>> getRecipeProgress(int recipeCode){
        return recipeProgressRepository.getRecipeProgress(recipeCode);
    }
}
