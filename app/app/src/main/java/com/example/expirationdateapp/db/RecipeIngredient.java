package com.example.expirationdateapp.db;

import androidx.room.Entity;

@Entity
public class RecipeIngredient {
    public int recipeCode;
    public int ingredientOrder;
    public String ingredientName;
    public String ingredientAmount;
    public int ingredientType;
    public String ingredientTypeName;
}
