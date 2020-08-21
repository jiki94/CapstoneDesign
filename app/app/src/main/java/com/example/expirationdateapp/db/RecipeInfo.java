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

    public RecipeInfo(int recipeCode, String recipeName, String recipeBasicDesc, Integer cuisineCode,
                      String cuisineDesc, Integer foodTypeCode, String foodType, String cookTime,
                      String calories, String servings, String skillLevel, String ingredientType,
                      String costToMake, String mainImgUrl, String detailUrl) {
        this.recipeCode = recipeCode;
        this.recipeName = recipeName;
        this.recipeBasicDesc = recipeBasicDesc;
        this.cuisineCode = cuisineCode;
        this.cuisineDesc = cuisineDesc;
        this.foodTypeCode = foodTypeCode;
        this.foodType = foodType;
        this.cookTime = cookTime;
        this.calories = calories;
        this.servings = servings;
        this.skillLevel = skillLevel;
        this.ingredientType = ingredientType;
        this.costToMake = costToMake;
        this.mainImgUrl = mainImgUrl;
        this.detailUrl = detailUrl;
    }

    public RecipeInfo(RecipeInfo other) {
        this.recipeCode = other.recipeCode;
        this.recipeName = other.recipeName;
        this.recipeBasicDesc = other.recipeBasicDesc;
        this.cuisineCode = other.cuisineCode;
        this.cuisineDesc = other.cuisineDesc;
        this.foodTypeCode = other.foodTypeCode;
        this.foodType = other.foodType;
        this.cookTime = other.cookTime;
        this.calories = other.calories;
        this.servings = other.servings;
        this.skillLevel = other.skillLevel;
        this.ingredientType = other.ingredientType;
        this.costToMake = other.costToMake;
        this.mainImgUrl = other.mainImgUrl;
        this.detailUrl = other.detailUrl;
    }
}
