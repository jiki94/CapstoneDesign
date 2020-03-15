package com.example.expirationdateapp.ui.viewmodel;

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

    public LiveData<List<Product>> getBasketItems(){
        return productRepository.getBasketItems();
    }

    public void deleteBasketItem(Product deleted){
        productRepository.deleteItem(deleted);
    }

    public void deleteAllItems(){
        productRepository.deleteAllBasketItems();
    }

    public void moveBasketToProducts(){
        productRepository.moveBasketToProducts();
    }
}


