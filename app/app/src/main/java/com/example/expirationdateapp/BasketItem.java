package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class BasketItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @NonNull
    public String name;

    @NonNull
    public String expiryDate;

    @NonNull
    @TypeConverters(StoredTypeConverter.class)
    public StoredType stored;

    // id 는 언제나 0으로 사용해야됨
    // autoGenerate 되서
    public BasketItem(int id, String name, String expiryDate, StoredType stored){
        this.name = name;
        this.expiryDate = expiryDate;
        this.stored = stored;
    }

    @Ignore
    public BasketItem(String name, String expiryDate, StoredType stored){
        this(0, name, expiryDate, stored);
    }
}

