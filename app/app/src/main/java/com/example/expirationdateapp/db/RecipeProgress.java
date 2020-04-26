package com.example.expirationdateapp.db;

import androidx.room.Entity;

@Entity(primaryKeys = {"recipeCode", "recipeOrder"})
public class RecipeProgress {
    public int recipeCode;
    public int recipeOrder;
    public String recipeDesc;
    public String progressPicture;
    public String progressTip;
}
