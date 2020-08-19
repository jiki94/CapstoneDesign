package com.example.expirationdateapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.TypeConverters;

import org.threeten.bp.LocalDate;

import java.util.List;

@Dao
@TypeConverters({LocalDateConverter.class})
public interface RecipeInfoDao {
    @Query("SELECT * FROM RecipeInfo WHERE recipeCode = :recipeCode")
    LiveData<RecipeInfo> getRecipeInfo(int recipeCode);

    // 유통기한 임박 기준 변경은 SELECT strftime('%s','now', '+3 days') 에서 +3 대신 교체
    @Query("SELECT r.*, IFNULL(aop.almostExpired, 0) as isAlmost FROM RecipeInfo as r " +
            "INNER JOIN (SELECT mi.recipeCode , IFNULL(CAST(havecount as DOUBLE)/allCount, 0) as percentage from " +
            "(SELECT recipeCode, count as allCount FROM MainIngredientCount " +
            " WHERE recipeCode NOT IN (SELECT * FROM DislikedRecipe)) as mi " +
            "LEFT JOIN (SELECT recipeCode, count(*) as havecount FROM RecipeIngredient " +
            "WHERE ingredientName IN " +
            "(SELECT name FROM PRODUCT WHERE inBasket = 0 AND " +
            "expiryDate >= :now) AND " +
            "ingredientType = 3060001 GROUP BY recipeCode) as ri " +
            "ON ri.recipeCode = mi.recipeCode) as pi " +
            "ON r.recipeCode = pi.recipeCode " +
            "LEFT JOIN (SELECT DISTINCT recipeCode, 1 as almostExpired FROM RecipeIngredient WHERE ingredientType = 3060001 AND " +
            "ingredientName IN (SELECT name FROM PRODUCT WHERE inBasket = 0 AND expiryDate >= :now AND " +
            "expiryDATE <= :imminentExpiry)) as aop " +
            "ON r.recipeCode = aop.recipeCode " +
            "ORDER BY almostExpired DESC, percentage DESC")
    LiveData<List<RecipeInfoAndAlmost>> getRecommendRecipes(LocalDate now, LocalDate imminentExpiry);

    @Query("SELECT r.*, IFNULL(aop.almostExpired, 0) as isAlmost FROM RecipeInfo as r " +
            "INNER JOIN (SELECT mi.recipeCode , IFNULL(CAST(havecount as DOUBLE)/allCount, 0) as percentage from " +
            "(SELECT recipeCode, count as allCount FROM MainIngredientCount " +
            " WHERE recipeCode NOT IN (SELECT * FROM DislikedRecipe)) as mi " +
            "LEFT JOIN (SELECT recipeCode, count(*) as havecount FROM RecipeIngredient " +
            "WHERE ingredientName IN " +
            "(SELECT name FROM PRODUCT WHERE inBasket = 0 AND " +
            "expiryDate >= :now) AND " +
            "ingredientType = 3060001 GROUP BY recipeCode) as ri " +
            "ON ri.recipeCode = mi.recipeCode) as pi " +
            "ON r.recipeCode = pi.recipeCode " +
            "LEFT JOIN (SELECT DISTINCT recipeCode, 1 as almostExpired FROM RecipeIngredient WHERE ingredientType = 3060001 AND " +
            "ingredientName IN (SELECT name FROM PRODUCT WHERE inBasket = 0 AND expiryDate >= :now AND " +
            "expiryDATE <= :imminentExpiry)) as aop " +
            "ON r.recipeCode = aop.recipeCode " +
            "WHERE instr(recipeName, :givenString) > 0 " +
            "AND r.recipeCode NOT IN (SELECT * FROM DislikedRecipe) " +
            "ORDER BY almostExpired DESC, percentage DESC ")
    LiveData<List<RecipeInfoAndAlmost>> getRecommendRecipes(String givenString, LocalDate now, LocalDate imminentExpiry);

    @Query("SELECT * FROM RecipeInfo WHERE recipeCode IN (SELECT * FROM DislikedRecipe)")
    LiveData<List<RecipeInfo>> getDislikedRecipes();

}
