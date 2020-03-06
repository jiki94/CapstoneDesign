package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class Favorite {
    @PrimaryKey
    @NonNull
    public String name;

    @NonNull
    @TypeConverters(StoredTypeConverter.class)
    public StoredType stored;

    public Favorite(String name, StoredType stored){
        this.name = name;
        this.stored = stored;
    }

    @Override
    @NonNull
    public String toString(){
        return name + " " + stored.toString();
    }
}
