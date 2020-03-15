package com.example.expirationdateapp.db;

import androidx.room.TypeConverter;

// DB 에서 enum 을 int 로 변환시 필요
public class StoredTypeConverter{
    @TypeConverter
    public static StoredType intToStoredType(int value){
        switch (value){
            case 0:
                return StoredType.COLD;
            case 1:
                return StoredType.FROZEN;
            case 2:
                return StoredType.ELSE;
            default:
                throw new IllegalArgumentException(StoredType.class.getName() + " cannot convert for " + value + " value.");
        }
    }
    @TypeConverter
    public static int storedTypeToInt(StoredType storedType){
        switch (storedType){
            case COLD:
                return 0;
            case FROZEN:
                return 1;
            case ELSE:
                return 2;
            default:
                throw new IllegalArgumentException("Added new values to " + StoredType.class.getName() + " Enum that is not handled.");
        }
    }
}

