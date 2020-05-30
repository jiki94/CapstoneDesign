package com.example.expirationdateapp.add.stt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.expirationdateapp.db.SttFoodData;
import com.example.expirationdateapp.db.SttFoodDataDao;
import com.example.expirationdateapp.db.SttFoodDataRepository;

import java.util.List;

public class SttViewModel extends ViewModel {
    private SttFoodDataRepository sttFoodDataRepository;

    public SttViewModel(SttFoodDataRepository sttFoodDataRepository){
        this.sttFoodDataRepository = sttFoodDataRepository;
    }

    public LiveData<List<SttFoodData>> getData(){
        return sttFoodDataRepository.getData();
    }
}
