package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;

import org.threeten.bp.LocalDate;

import java.util.List;

public class ProductRepository {
    private AppRoomDatabase database;
    private ProductDao productDao;
    private LiveData<List<Product>> basketItems;
    private LiveData<List<Product>> coldItems;
    private LiveData<List<Product>> frozenItems;
    private LiveData<List<Product>> otherItems;
    private LiveData<List<Product>> overdueItems;

    public ProductRepository(AppRoomDatabase database){
        LocalDate now = LocalDate.now();

        this.database = database;
        productDao = this.database.productDao();
        basketItems = productDao.getItems(true);
        coldItems = productDao.getItemsNotOverdue(false, StoredType.COLD, now);
        frozenItems = productDao.getItemsNotOverdue(false, StoredType.FROZEN, now);
        otherItems = productDao.getItemsNotOverdue(false, StoredType.ELSE, now);
        overdueItems = productDao.getOverdueItems(false, now);
    }

    public LiveData<List<Product>> getBasketItems(){
        return basketItems;
    }

    public LiveData<List<Product>> getItemsByStoredType(StoredType storedType){
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

    public LiveData<List<Product>> getOverdueItems(){
        return overdueItems;
    }

    public void insertItem(final Product newItem){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                productDao.addProduct(newItem);
            }
        });
    }

    public void deleteItem(final Product deletedItem){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                productDao.deleteItem(deletedItem);
            }
        });
    }

    public void deleteAllBasketItems(){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                productDao.deleteItems(true);
            }
        });
    }

    public void updateItem(final Product updatedItem){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                productDao.updateProduct(updatedItem);
            }
        });
    }

    public void moveBasketToProducts(){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                productDao.updateInBasket(false, true);
            }
        });
    }

    public Product getItem(int id){
        return productDao.getItem(id);
    }
}
