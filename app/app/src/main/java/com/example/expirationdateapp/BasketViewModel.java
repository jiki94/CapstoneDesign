package com.example.expirationdateapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class BasketViewModel extends ViewModel {
    private final BasketItemRepository basketItemRepository;

    BasketViewModel(BasketItemRepository basketItemRepository){
        this.basketItemRepository = basketItemRepository;
    }

    LiveData<List<BasketItem>> getBasketItems(){
        return basketItemRepository.getBasketItems();
    }

    void deleteBasketItem(BasketItem deleted){
        basketItemRepository.deleteBasketItem(deleted);
    }

    void deleteAllItems(){
        basketItemRepository.deleteAllBasketItems();
    }
}


