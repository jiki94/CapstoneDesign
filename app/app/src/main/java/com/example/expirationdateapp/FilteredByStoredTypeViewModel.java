package com.example.expirationdateapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

// FilteredByStoredTypeFragment 와 연결된 ViewModel
class FilteredByStoredTypeViewModel extends ViewModel {
    private ProductRepository productRepository;

    FilteredByStoredTypeViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    LiveData<List<Product>> getItemsByStoredType(StoredType storedType){
        return productRepository.getItemsByStoredType(storedType);
    }

    void deleteProduct(Product toDel){
        productRepository.deleteItem(toDel);
    }
}
