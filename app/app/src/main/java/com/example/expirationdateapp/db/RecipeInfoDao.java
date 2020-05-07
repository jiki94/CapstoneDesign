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

    @Query("SELECT r.* FROM RecipeInfo as r " +
            "INNER JOIN (SELECT mi.recipeCode , IFNULL(CAST(havecount as DOUBLE)/allCount, 0) as percentage from " +
            "(SELECT recipeCode, count as allCount FROM MainIngredientCount) as mi " +
            "LEFT JOIN (SELECT recipeCode, count(*) as havecount FROM RecipeIngredient " +
            "WHERE ingredientName IN " +
            "(SELECT name FROM PRODUCT WHERE inBasket = 0 AND " +
            "expiryDate >= :now) AND " +
            "ingredientType = 3060001 GROUP BY recipeCode) as ri " +
            "ON ri.recipeCode = mi.recipeCode) as pi " +
            "ON r.recipeCode = pi.recipeCode " +
            "ORDER BY percentage DESC")
    LiveData<List<RecipeInfo>> getRecommendRecipes(LocalDate now);
}
