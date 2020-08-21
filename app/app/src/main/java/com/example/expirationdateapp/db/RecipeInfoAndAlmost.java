package com.example.expirationdateapp.db;

// 레시피 정보와 유통기한 임박인지 정보 있는 클래슨
public class RecipeInfoAndAlmost extends RecipeInfo {
    public boolean isAlmost;

    public RecipeInfoAndAlmost(int recipeCode, String recipeName, String recipeBasicDesc, Integer cuisineCode,
                      String cuisineDesc, Integer foodTypeCode, String foodType, String cookTime,
                      String calories, String servings, String skillLevel, String ingredientType,
                      String costToMake, String mainImgUrl, String detailUrl, boolean isAlmost) {
        super(recipeCode, recipeName, recipeBasicDesc, cuisineCode, cuisineDesc, foodTypeCode, foodType, cookTime,
                calories, servings, skillLevel, ingredientType, costToMake, mainImgUrl, detailUrl);
        this.isAlmost = isAlmost;
    }

    public RecipeInfoAndAlmost(RecipeInfo recipeInfo, boolean isAlmost) {
        super(recipeInfo);
        this.isAlmost = isAlmost;
    }
}
