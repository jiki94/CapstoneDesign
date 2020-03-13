package com.example.expirationdateapp.db;

import com.example.expirationdateapp.R;

public enum StoredType {
    COLD, FROZEN, ELSE;

    public int getStringId(){
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

