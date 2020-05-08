package com.example.expirationdateapp.recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.expirationdateapp.db.RecipeInfo;
import com.example.expirationdateapp.db.RecipeInfoRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
    private RecipeInfoRepository recipeInfoRepository;
    private MediatorLiveData<List<RecipeInfo>> showingRecipes;
    private LiveData<List<RecipeInfo>> recommendRecipes;
    private LiveData<List<RecipeInfo>> searchedRecipes;
    private String searchWord;

    public RecipeListViewModel(RecipeInfoRepository recipeInfoRepository){
        this.recipeInfoRepository = recipeInfoRepository;

        searchWord = "";
        recommendRecipes = recipeInfoRepository.getRecommendRecipes();
        searchedRecipes = recipeInfoRepository.getRecipeInfoSearchBy(searchWord);

        showingRecipes = new MediatorLiveData<>();
        showingRecipes.addSource(recommendRecipes, new Observer<List<RecipeInfo>>() {
            @Override
            public void onChanged(List<RecipeInfo> newValue) {
                List<RecipeInfo> retData = onDataChange();
                if (retData != null)
                    showingRecipes.setValue(retData);
            }
        });

        showingRecipes.addSource(searchedRecipes, new Observer<List<RecipeInfo>>() {
            @Override
            public void onChanged(List<RecipeInfo> newValue) {
                List<RecipeInfo> retData = onDataChange();
                if (retData != null)
                    showingRecipes.setValue(retData);
            }
        });
    }

    private List<RecipeInfo> onDataChange(){
        if (searchWord == null || searchWord.isEmpty())
            return recommendRecipes.getValue();
        else
            return searchedRecipes.getValue();
    }

    void changeSearchWord(String word){
        showingRecipes.removeSource(searchedRecipes);

        searchWord = word;
        searchedRecipes = recipeInfoRepository.getRecipeInfoSearchBy(word);

        showingRecipes.addSource(searchedRecipes, new Observer<List<RecipeInfo>>() {
            @Override
            public void onChanged(List<RecipeInfo> newValue) {
                List<RecipeInfo> retData = onDataChange();
                if (retData != null)
                    showingRecipes.setValue(retData);
            }
        });
    }

    LiveData<List<RecipeInfo>> getShowingRecipes(){
        return showingRecipes;
    }
}
