package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SttFoodDataDao {
    @Query("SELECT * FROM SttFoodData")
    LiveData<List<SttFoodData>> getData();

    @Insert(entity=SttFoodData.class, onConflict = OnConflictStrategy.IGNORE)
    void addingNames(List<SttFoodData> data);
}
