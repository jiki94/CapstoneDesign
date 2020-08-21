package com.example.expirationdateapp.recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.expirationdateapp.db.RecipeInfo;
import com.example.expirationdateapp.db.RecipeInfoAndAlmost;
import com.example.expirationdateapp.db.RecipeInfoRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
    private RecipeInfoRepository recipeInfoRepository;
    private MediatorLiveData<List<RecipeInfoAndAlmost>> showingRecipes;
    private LiveData<List<RecipeInfoAndAlmost>> recommendRecipes;
    private LiveData<List<RecipeInfoAndAlmost>> searchedRecipes;

    private String searchWord;

    public RecipeListViewModel(RecipeInfoRepository recipeInfoRepository){
        this.recipeInfoRepository = recipeInfoRepository;

        searchWord = "";
        recommendRecipes = recipeInfoRepository.getRecommendRecipes();
        searchedRecipes = null;

        showingRecipes = new MediatorLiveData<>();
        showingRecipes.addSource(recommendRecipes, newValue -> {
            List<RecipeInfoAndAlmost> retData = onDataChange();
            if (retData != null)
                showingRecipes.setValue(retData);
        });
    }

    private List<RecipeInfoAndAlmost> onDataChange(){
        if (searchWord == null || searchWord.isEmpty())
            return recommendRecipes.getValue();
        else
            return searchedRecipes.getValue();
    }

    void changeSearchWord(String word){
        searchWord = word;

        if (searchedRecipes != null) {
            showingRecipes.removeSource(searchedRecipes);
            searchedRecipes = null;
        }

        if (searchWord.isEmpty()) {
            return;
        }

        searchedRecipes = recipeInfoRepository.getRecipeInfoSearchBy(word);

        showingRecipes.addSource(searchedRecipes, newValue -> {
            List<RecipeInfoAndAlmost> retData = onDataChange();
            if (retData != null)
                showingRecipes.setValue(retData);
        });
    }

    LiveData<List<RecipeInfoAndAlmost>> getShowingRecipes(){
        return showingRecipes;
    }
}
