package com.example.expirationdateapp.db;

public class DislikedRecipeRepository {
    private AppRoomDatabase db;
    private DislikedRecipeDao dislikedRecipeDao;

    public DislikedRecipeRepository(AppRoomDatabase db) {
        this.db = db;
        this.dislikedRecipeDao = db.dislikedRecipeDao();
    }

    public void addDisliked(int recipeCode){
        db.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dislikedRecipeDao.insertDisliked(new DislikedRecipe(recipeCode));
            }
        });
    }

    public void deleteDisliked(int recipeCode){
        db.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dislikedRecipeDao.deleteDisliked(new DislikedRecipe(recipeCode));
            }
        });
    }
}
