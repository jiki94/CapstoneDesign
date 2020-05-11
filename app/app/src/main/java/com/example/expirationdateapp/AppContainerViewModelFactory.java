package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.expirationdateapp.add.AddViewModel;
import com.example.expirationdateapp.add.basket.BasketViewModel;
import com.example.expirationdateapp.recipe.DislikedListViewModel;
import com.example.expirationdateapp.recipe.RecipeDetailViewModel;
import com.example.expirationdateapp.recipe.RecipeListViewModel;
import com.example.expirationdateapp.viewing.ViewTabViewModel;

// db 관련이나 AppContainer 가 필요한 ViewModel Factory
public class AppContainerViewModelFactory implements ViewModelProvider.Factory{
    private AppContainer appContainer;

    public AppContainerViewModelFactory(AppContainer appContainer){
        this.appContainer = appContainer;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(AddViewModel.class))
            return (T) new AddViewModel(appContainer.getFavoriteRepository(),
                    appContainer.getProductRepository());
        else if (modelClass.equals(BasketViewModel.class))
            return (T) new BasketViewModel(appContainer.getProductRepository());
        else if (modelClass.equals(ViewTabViewModel.class))
            return (T) new ViewTabViewModel(appContainer.getProductRepository());
        else if (modelClass.equals(RecipeListViewModel.class))
            return (T) new RecipeListViewModel(appContainer.getRecipeInfoRepository());
        else if (modelClass.equals(RecipeDetailViewModel.class))
            return (T) new RecipeDetailViewModel(appContainer.getRecipeInfoRepository(), appContainer.getRecipeIngredientRepository(),
                    appContainer.getRecipeProgressRepository(), appContainer.getDislikedRecipeRepository());
        else if (modelClass.equals(DislikedListViewModel.class))
            return (T) new DislikedListViewModel(appContainer.getRecipeInfoRepository());

        throw new IllegalArgumentException("There is factory does not support " + modelClass.getName());
    }
}