package com.example.expirationdateapp.add.basket;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.expirationdateapp.alarm.AlarmSetter;
import com.example.expirationdateapp.db.Product;
import com.example.expirationdateapp.db.ProductRepository;

import java.util.List;

// BasketActivity 랑 연결된 ViewModel
public class BasketViewModel extends AndroidViewModel {
    private final ProductRepository productRepository;

    public BasketViewModel(@NonNull Application application, ProductRepository productRepository){
        super(application);
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

    void moveBasketToProducts(List<Product> basket){
        AlarmSetter alarmSetter = new AlarmSetter(getApplication());
        for (Product basketItem : basket){
            alarmSetter.setAlarm(basketItem);
        }

        productRepository.moveBasketToProducts();
    }
}


