package com.example.expirationdateapp.viewing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expirationdateapp.db.Product;
import com.example.expirationdateapp.db.ProductRepository;
import com.example.expirationdateapp.db.StoredType;

import java.util.List;

// ViewTabFragment 와 연결된 ViewModel
public class ViewTabViewModel extends ViewModel {
    private ProductRepository productRepository;
    private MutableLiveData<String> filterString;

    public ViewTabViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
        filterString = new MutableLiveData<>();
    }

    public LiveData<List<Product>> getItemsByCategory(ViewCategory category){
        StoredType storedType = category.getAssociatedStoredType();
        if (storedType == null){
            return productRepository.getOverdueItems();
        }else{
            return productRepository.getItemsByStoredType(storedType);
        }
    }

    public void deleteProduct(Product toDel){
        productRepository.deleteItem(toDel);
    }

    public LiveData<String> getFilterString(){
        return filterString;
    }

    public void setFilterString(String newFilterString){
        filterString.postValue(newFilterString);
    }
}
