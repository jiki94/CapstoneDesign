package com.example.expirationdateapp.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MainIngredientCount {
    @PrimaryKey
    @NonNull
    public int recipeCode;

    @NonNull
    public int count;
}
