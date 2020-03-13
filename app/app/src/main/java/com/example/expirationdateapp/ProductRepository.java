package com.example.expirationdateapp;

import androidx.lifecycle.LiveData;

import java.util.List;

class ProductRepository {
    private AppRoomDatabase database;
    private ProductDao productDao;
    private LiveData<List<Product>> basketItems;
    private LiveData<List<Product>> coldItems;
    private LiveData<List<Product>> frozenItems;
    private LiveData<List<Product>> otherItems;

    ProductRepository(AppRoomDatabase database){
        this.database = database;
        productDao = this.database.productDao();
        basketItems = productDao.getItems(true);
        coldItems = productDao.getItems(false, StoredType.COLD);
        frozenItems = productDao.getItems(false, StoredType.FROZEN);
        otherItems = productDao.getItems(false, StoredType.ELSE);
    }

    LiveData<List<Product>> getBasketItems(){
        return basketItems;
    }

    // one function return cold, froze, other by one parameter
    LiveData<List<Product>> getItemsByStoredType(StoredType storedType){
        switch (storedType){
            case COLD:
                return coldItems;
            case FROZEN:
                return frozenItems;
            case ELSE:
                return otherItems;

            default:
                throw new IllegalArgumentException();
        }
    }

    void insertItem(final Product newItem){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                productDao.addProduct(newItem);
            }
        });
    }

    void deleteItem(final Product deletedItem){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                productDao.deleteItem(deletedItem);
            }
        });
    }

    void deleteAllBasketItems(){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                productDao.deleteItems(true);
            }
        });
    }

    void updateItem(final Product updatedItem){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                productDao.updateProduct(updatedItem);
            }
        });
    }

    void moveBasketToProducts(){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                productDao.updateInBasket(false, true);
            }
        });
    }
}
