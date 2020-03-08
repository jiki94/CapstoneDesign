package com.example.expirationdateapp;

import androidx.lifecycle.LiveData;

import java.util.List;

class BasketItemRepository {
    private AppRoomDatabase database;
    private BasketItemDao basketItemDao;
    private LiveData<List<BasketItem>> basketItems;

    BasketItemRepository(AppRoomDatabase database){
        this.database = database;
        basketItemDao = this.database.basketItemDao();
        basketItems = basketItemDao.getBasketItems();
    }

    LiveData<List<BasketItem>> getBasketItems(){
        return basketItems;
    }

    void insertBasketItem(final BasketItem newBasketItem){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                basketItemDao.addBasketItem(newBasketItem);
            }
        });
    }

    void deleteBasketItem(final BasketItem deletedItem){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                basketItemDao.deleteBasketItem(deletedItem);
            }
        });
    }

    void deleteAllBasketItems(){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                basketItemDao.deleteAllBasketItems();
            }
        });
    }

    void updateBasketItem(final BasketItem updatedItem){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                basketItemDao.updateBasketItem(updatedItem);
            }
        });
    }
}
