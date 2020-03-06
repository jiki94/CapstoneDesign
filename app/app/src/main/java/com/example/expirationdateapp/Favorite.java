package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Favorite {
    @PrimaryKey
    public String name;

    @NonNull
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
