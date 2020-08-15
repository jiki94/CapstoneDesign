package com.example.expirationdateapp.db;

// 레시피 정보와 유통기한 임박인지 정보 있는 클래슨
public class RecipeInfoAndAlmost extends RecipeInfo {
    public boolean isAlmost;

    public RecipeInfoAndAlmost(RecipeInfo recipeInfo, boolean isAlmost) {
        super(recipeInfo);
        this.isAlmost = isAlmost;
    }
}
