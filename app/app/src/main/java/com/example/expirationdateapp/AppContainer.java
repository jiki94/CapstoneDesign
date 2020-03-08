package com.example.expirationdateapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class AppContainer {
    private AppRoomDatabase database;
    FavoriteRepository favoriteRepository;
    BasketItemRepository basketItemRepository;

    public AppContainer(Context context){
        database = Room.databaseBuilder(context.getApplicationContext(), AppRoomDatabase.class, "app_room_database_testing")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<Favorite> data = new ArrayList<>(Arrays.asList(
                                    new Favorite("우유", StoredType.FROZEN),
                                    new Favorite("피자", StoredType.COLD),
                                    new Favorite("물", StoredType.ELSE),
                                    new Favorite("치킨", StoredType.COLD),
                                    new Favorite("배추", StoredType.COLD),
                                    new Favorite("설탕", StoredType.ELSE),
                                    new Favorite("아이스크림", StoredType.COLD)
                                    ));

                                for (Favorite favorite : data)
                                    database.favoriteDao().insertFavorite(favorite);
                            }
                        });
                    }
                }).build();
        //database = Room.databaseBuilder(context.getApplicationContext(), AppRoomDatabase.class, "app_room_database").build();
        favoriteRepository = new FavoriteRepository(database);
        basketItemRepository = new BasketItemRepository(database);
    }
}
