package com.example.expirationdateapp.db;

import androidx.room.Entity;

@Entity
public class RecipeInfo {
    public int recipeCode;
    public String recipeName;
    public String recipeBasicDesc;
    public int cuisineCode;
    public String cuisineDesc;
    public int foodTypeCode;
    public String foodType;
    public String cookTime;
    public String calories;
    public String servings;
    public String skillLevel;
    public String ingredientType;
    public String costToMake;
    public String mainImgUrl;
    public String detailUrl;
}
