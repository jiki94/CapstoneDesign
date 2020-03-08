package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

class AddViewModel extends ViewModel {
    private final FavoriteRepository favoriteRepository;
    private final BasketItemRepository basketItemRepository;

    AddViewModel(FavoriteRepository favoriteRepository, BasketItemRepository basketItemRepository){
        this.favoriteRepository = favoriteRepository;
        this.basketItemRepository = basketItemRepository;
    }

    LiveData<List<Favorite>> getFavorites(){
        return favoriteRepository.getFavorites();
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

    void insertBasketItem(BasketItem newItem){
        basketItemRepository.insertBasketItem(newItem);
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
            return (T) new AddViewModel(appContainer.favoriteRepository, appContainer.basketItemRepository);

        throw new IllegalArgumentException("There is factory does not support " + modelClass.getName());
    }
}