package com.example.expirationdateapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BasketItemDao {
    @Insert
    void addBasketItem(BasketItem basketItem);

    @Query("SELECT * FROM BasketItem")
    LiveData<List<BasketItem>> getBasketItems();

    @Delete
    void deleteBasketItem(BasketItem basketItem);

    @Query("DELETE FROM BasketItem")
    void deleteAllBasketItems();

    @Update
    void updateBasketItem(BasketItem basketItem);
}
