package com.example.expirationdateapp.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.expirationdateapp.db.Favorite;
import com.example.expirationdateapp.db.FavoriteRepository;
import com.example.expirationdateapp.db.Product;
import com.example.expirationdateapp.db.ProductRepository;

import java.util.List;

// AddFragment 이랑 연동되는 ViewModel
public class AddViewModel extends ViewModel {
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    public AddViewModel(FavoriteRepository favoriteRepository, ProductRepository productRepository){
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
    }

    public LiveData<List<Favorite>> getFavorites(){
        return favoriteRepository.getFavorites();
    }
    public void insertFavorite(Favorite newRecord) {
        favoriteRepository.insertFavorite(newRecord);
    }
    public void deleteFavoriteByName(String name){
        favoriteRepository.deleteByName(name);
    }
    public void updateFavorite(Favorite updated){
        favoriteRepository.updateFavorite(updated);
    }
    public void insertBasketItem(Product newBasketItem){
        productRepository.insertItem(newBasketItem);
    }
}

