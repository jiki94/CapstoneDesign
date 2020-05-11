package com.example.expirationdateapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {
    Favorite.class,
    Product.class,
    RecipeInfo.class,
    RecipeIngredient.class,
    RecipeProgress.class,
    MainIngredientCount.class,
    DislikedRecipe.class
    },
    version =  1,
    exportSchema = false
)
public abstract class AppRoomDatabase extends RoomDatabase{
    public abstract FavoriteDao favoriteDao();
    public abstract ProductDao productDao();
    public abstract RecipeInfoDao recipeInfoDao();
    public abstract RecipeIngredientDao recipeIngredientDao();
    public abstract RecipeProgressDao recipeProgressDao();
    public abstract MainIngredientCountDao mainIngredientCountDao();
    public abstract DislikedRecipeDao dislikedRecipeDao();

    private static final int NUMBER_OF_THREADS = 4;

    // db에 사용할 때 사용하는 thread pool
    final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
}
