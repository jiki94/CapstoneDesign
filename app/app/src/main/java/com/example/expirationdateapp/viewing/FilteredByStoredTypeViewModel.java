package com.example.expirationdateapp.viewing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.expirationdateapp.db.Product;
import com.example.expirationdateapp.db.ProductRepository;
import com.example.expirationdateapp.db.StoredType;

import java.util.List;

// FilteredByStoredTypeFragment 와 연결된 ViewModel
public class FilteredByStoredTypeViewModel extends ViewModel {
    private ProductRepository productRepository;

    public FilteredByStoredTypeViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public LiveData<List<Product>> getItemsByStoredType(StoredType storedType){
        return productRepository.getItemsByStoredType(storedType);
    }

    public void deleteProduct(Product toDel){
        productRepository.deleteItem(toDel);
    }
}
