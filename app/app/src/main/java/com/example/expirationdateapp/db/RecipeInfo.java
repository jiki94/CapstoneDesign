package com.example.expirationdateapp.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecipeInfo {
    @PrimaryKey
    public int recipeCode;
    public String recipeName;
    public String recipeBasicDesc;
    public Integer cuisineCode;
    public String cuisineDesc;
    public Integer foodTypeCode;
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
