package com.example.expirationdateapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

class AddViewModel extends ViewModel {
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    AddViewModel(FavoriteRepository favoriteRepository, ProductRepository productRepository){
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
    }

    LiveData<List<Favorite>> getFavorites(){
        return favoriteRepository.getFavorites();
    }

    void insertFavorite(Favorite newRecord) {
        favoriteRepository.insertFavorite(newRecord);
    }

    void deleteFavoriteByName(String name){
        favoriteRepository.deleteByName(name);
    }

    void updateFavorite(Favorite updated){
        favoriteRepository.updateFavorite(updated);
    }

    void insertBasketItem(Product newBasketItem){
        productRepository.insertItem(newBasketItem);
    }
}

