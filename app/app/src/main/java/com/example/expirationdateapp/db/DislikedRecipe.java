package com.example.expirationdateapp.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DislikedRecipe {
    @PrimaryKey
    int recipeCode;

    public DislikedRecipe(int recipeCode) {
        this.recipeCode = recipeCode;
    }
}
