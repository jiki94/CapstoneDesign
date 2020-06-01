package com.example.expirationdateapp.db;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SttFoodDataRepository {
    @NonNull private AppRoomDatabase db;
    private SttFoodDataDao sttFoodDataDao;
    private LiveData<List<SttFoodData>> data;

    public SttFoodDataRepository(@NonNull AppRoomDatabase db){
        this.db = db;
        sttFoodDataDao = db.sttFoodDataDao();
        data = sttFoodDataDao.getData();
    }

    public LiveData<List<SttFoodData>> getData(){
        return data;
    }
}
