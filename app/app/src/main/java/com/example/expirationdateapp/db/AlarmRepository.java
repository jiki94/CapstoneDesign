package com.example.expirationdateapp.db;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class AlarmRepository {
    private AppRoomDatabase database;
    private AlarmDao alarmDao;

    public AlarmRepository(AppRoomDatabase database){
        this.database = database;
        this.alarmDao = database.alarmDao();
    }

    public Maybe<List<Alarm>> getAlarms(){
        return alarmDao.getAlarms();
    }

    public void addAlarm(Alarm alarm){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                alarmDao.addAlarm(alarm);
            }
        });
    }

    public void deleteAlarm(Alarm alarm){
        database.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                alarmDao.deleteAlarm(alarm);
            }
        });
    }
}
