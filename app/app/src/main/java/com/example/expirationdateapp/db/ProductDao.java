package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.Update;

import org.threeten.bp.LocalDate;

import java.util.List;

@Dao
@TypeConverters({StoredTypeConverter.class, LocalDateConverter.class})
public interface ProductDao {
    @Insert()
    void addProduct(Product product);

    @Query("SELECT * FROM Product WHERE inBasket = :inBasket")
    LiveData<List<Product>> getItems(boolean inBasket);

    @Query("SELECT * FROM Product WHERE inBasket = :inBasket AND stored = :storedType")
    LiveData<List<Product>> getItems(boolean inBasket, StoredType storedType);

    @Query("SELECT * FROM Product WHERE inBasket = :inBasket AND stored = :storedType AND expiryDate >= :now")
    LiveData<List<Product>> getItemsNotOverdue(boolean inBasket, StoredType storedType, LocalDate now);

    @Query("SELECT DISTINCT name FROM Product WHERE inBasket = 0 AND expiryDate >= :now")
    LiveData<List<String>> getItemsNotOverdue(LocalDate now);

    @Query("SELECT DISTINCT name FROM Product WHERE inBasket = 0 AND expiryDate >= :now AND " +
            " expiryDate <= :imminentExpiry")
    LiveData<List<String>> getImminentExpiry(LocalDate now, LocalDate imminentExpiry);

    @Query("SELECT * FROM Product WHERE inBasket = :inBasket AND expiryDate < :now")
    LiveData<List<Product>> getOverdueItems(boolean inBasket, LocalDate now);

    // 백그라운드에서 호출할 때 사용
    @Query("SELECT * FROM Product WHERE id = :id")
    Product getItem(int id);

    @Delete
    void deleteItem(Product product);

    @Query("DELETE FROM Product WHERE inBasket = :inBasket")
    void deleteItems(boolean inBasket);

    @Update
    void updateProduct(Product product);

    @Query("UPDATE Product SET inBasket = :newInBasket WHERE inBasket = :oldInBasket")
    void updateInBasket(boolean newInBasket, boolean oldInBasket);
}
