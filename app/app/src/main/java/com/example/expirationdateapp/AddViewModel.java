package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

class AddViewModel extends ViewModel {
    private final FavoriteRepository favoriteRepository;
    private LiveData<List<Favorite>> favorites;

    AddViewModel(FavoriteRepository favoriteRepository){
        this.favoriteRepository = favoriteRepository;
        favorites = this.favoriteRepository.getFavorites();
    }

    LiveData<List<Favorite>> getFavorites(){
        return favorites;
    }

    void insertFavorite(Favorite newRecord) {
        favoriteRepository.insertFavorite(newRecord);
    }

    void deleteByName(String name){
        favoriteRepository.deleteByName(name);
    }

    void updateFavorite(Favorite updated){
        favoriteRepository.updateFavorite(updated);
    }
}

class AppContainerViewModelFactory implements ViewModelProvider.Factory{
    private AppContainer appContainer;
    AppContainerViewModelFactory(AppContainer appContainer){
        this.appContainer = appContainer;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(AddViewModel.class))
            return (T) new AddViewModel(appContainer.favoriteRepository);

        throw new IllegalArgumentException("There is factory does not support " + modelClass.getName());
    }
}