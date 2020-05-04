package com.example.expirationdateapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.expirationdateapp.db.AppRoomDatabase;
import com.example.expirationdateapp.db.Favorite;
import com.example.expirationdateapp.db.FavoriteRepository;
import com.example.expirationdateapp.db.ProductRepository;
import com.example.expirationdateapp.db.RecipeInfoRepository;
import com.example.expirationdateapp.db.RecipeIngredientRepository;
import com.example.expirationdateapp.db.RecipeProgressRepository;
import com.example.expirationdateapp.db.StoredType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;

// Singleton 이야 하는 것들 저장하는 클래스
// Activity 상속하는 클래스에 들어가서 앱에서 한개의 인스턴스만 존재함
public class AppContainer {
    private AppRoomDatabase database;
    private FavoriteRepository favoriteRepository;
    private ProductRepository productRepository;
    private RecipeInfoRepository recipeInfoRepository;
    private RecipeIngredientRepository recipeIngredientRepository;
    private RecipeProgressRepository recipeProgressRepository;

    AppContainer(Context context){
        // 현재 db에 즐겨찾기 더미 데이터 있
        database = Room.databaseBuilder(context.getApplicationContext(), AppRoomDatabase.class, "app_room_database_testing")
                .createFromAsset("use.db")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<Favorite> data = new ArrayList<>(Arrays.asList(
                                    new Favorite("우유", StoredType.FROZEN, 7, false),
                                    new Favorite("피자", StoredType.COLD, 5, true),
                                    new Favorite("물", StoredType.ELSE, 8, true),
                                    new Favorite("치킨", StoredType.COLD, 9, true),
                                    new Favorite("배추", StoredType.COLD, 7, false),
                                    new Favorite("설탕", StoredType.ELSE, 10, true),
                                    new Favorite("아이스크림", StoredType.COLD, 4, true)
                                    ));

                                for (Favorite favorite : data)
                                    database.favoriteDao().insertFavorite(favorite);
                            }
                        });
                    }
                }).build();
        //database = Room.databaseBuilder(context.getApplicationContext(), AppRoomDatabase.class, "app_room_database").build();
        favoriteRepository = new FavoriteRepository(database);
        productRepository = new ProductRepository(database);
        recipeInfoRepository = new RecipeInfoRepository(database);
        recipeIngredientRepository = new RecipeIngredientRepository(database);
        recipeProgressRepository = new RecipeProgressRepository(database);
    }

    public FavoriteRepository getFavoriteRepository() {
        return favoriteRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public RecipeInfoRepository getRecipeInfoRepository() {
        return recipeInfoRepository;
    }

    public RecipeIngredientRepository getRecipeIngredientRepository(){
        return recipeIngredientRepository;
    }

    public RecipeProgressRepository getRecipeProgressRepository(){
        return recipeProgressRepository;
    }
}
