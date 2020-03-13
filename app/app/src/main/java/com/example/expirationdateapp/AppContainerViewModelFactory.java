package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

// db 관련이나 AppContainer 가 필요한 ViewModel Factory
class AppContainerViewModelFactory implements ViewModelProvider.Factory{
    private AppContainer appContainer;

    AppContainerViewModelFactory(AppContainer appContainer){
        this.appContainer = appContainer;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(AddViewModel.class))
            return (T) new AddViewModel(appContainer.getFavoriteRepository(), appContainer.getProductRepository());
        else if (modelClass.equals(BasketViewModel.class))
            return (T) new BasketViewModel(appContainer.getProductRepository());
        else if (modelClass.equals(FilteredByStoredTypeViewModel.class))
            return (T) new FilteredByStoredTypeViewModel(appContainer.getProductRepository());

        throw new IllegalArgumentException("There is factory does not support " + modelClass.getName());
    }
}