package com.example.expirationdateapp.add.basket;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.expirationdateapp.db.Product;
import com.example.expirationdateapp.db.ProductRepository;

import java.util.List;

// BasketActivity 랑 연결된 ViewModel
public class BasketViewModel extends ViewModel {
    private final ProductRepository productRepository;

    public BasketViewModel(ProductRepository productRepository){
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

