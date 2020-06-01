package com.example.expirationdateapp.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SttFoodData {
    @NonNull
    @PrimaryKey
    public String name;

    public SttFoodData(@NonNull String name){
        this.name = name;
    }
}
