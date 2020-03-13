package com.example.expirationdateapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class BasketViewModel extends ViewModel {
    private final ProductRepository productRepository;

    BasketViewModel(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    LiveData<List<Product>> getBasketItems(){
        return productRepository.getBasketItems();
    }

    void deleteBasketItem(Product deleted){
        productRepository.deleteItem(deleted);
    }

    void deleteAllItems(){
        productRepository.deleteAllBasketItems();
    }

    void moveBasketToProducts(){
        productRepository.moveBasketToProducts();
    }
}


