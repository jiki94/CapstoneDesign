package com.example.expirationdateapp.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"recipeCode", "ingredientOrder"})
public class RecipeIngredient {
    public final static int MAIN = 3060001;
    public final static int SUB = 3060002;
    public final static int SEASONING = 3060003;

    public int recipeCode;
    public int ingredientOrder;
    public String ingredientName;
    public String ingredientAmount;
    public Integer ingredientType;
    public String ingredientTypeName;
}
