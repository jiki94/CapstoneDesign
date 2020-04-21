package com.example.expirationdateapp.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity()
public class Alarm {
    @PrimaryKey
    @ForeignKey(entity = Product.class, parentColumns ="id", childColumns ="id")
    public int id;

    public Alarm(int id){
        this.id = id;
    }
}
