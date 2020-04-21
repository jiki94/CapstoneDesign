package com.example.expirationdateapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface AlarmDao {
    @Query("SELECT * FROM Alarm")
    Maybe<List<Alarm>> getAlarms();

    @Insert
    void addAlarm(Alarm alarm);

    @Delete
    void deleteAlarm(Alarm alarm);
}
