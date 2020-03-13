package com.example.expirationdateapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FilteredByStoredTypeViewModel extends ViewModel {
    private ProductRepository productRepository;

    public FilteredByStoredTypeViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
        Log.v("MAKING_VIEWMODEL", "made new viewmodel");
    }

    LiveData<List<Product>> getItemsByStoredType(StoredType storedType){
        return productRepository.getItemsByStoredType(storedType);
    }

    void deleteProduct(Product toDel){
        productRepository.deleteItem(toDel);
    }
}
