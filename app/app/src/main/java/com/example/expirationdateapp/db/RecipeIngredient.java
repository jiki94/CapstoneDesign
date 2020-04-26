package com.example.expirationdateapp.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"recipeCode", "ingredientOrder"})
public class RecipeIngredient {
    public int recipeCode;
    public int ingredientOrder;
    public String ingredientName;
    public String ingredientAmount;
    public Integer ingredientType;
    public String ingredientTypeName;
}
