package com.example.expirationdateapp;

import androidx.room.TypeConverter;

enum StoredType {
    COLD, FROZEN, ELSE;

    int getStringId(){
        switch (this){
            case COLD:
                return R.string.stored_cold;
            case FROZEN:
                return R.string.stored_frozen;
            case ELSE:
                return R.string.stored_else;
            default:
                throw new IllegalArgumentException(StoredType.class.getName() + " does not have such enum value");
        }
    }
}

class StoredTypeConverter{
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

