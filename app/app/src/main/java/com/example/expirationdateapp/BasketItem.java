package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        this.id = id;
        this.name = name;
        this.expiryDate = expiryDate;
        this.stored = stored;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        // id가 같은데 다른 content 가질 가능성 없을듯;
        BasketItem other = (BasketItem) obj;
        return id == other.id;
    }
}

