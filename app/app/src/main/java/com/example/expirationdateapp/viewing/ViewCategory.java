package com.example.expirationdateapp.viewing;

import com.example.expirationdateapp.db.StoredType;

enum ViewCategory {
    OVERDUE, COLD, FROZEN, ELSE;

    StoredType getAssociatedStoredType(){
        switch (this){
            case OVERDUE:
                return null;
            case COLD:
                return StoredType.COLD;
            case FROZEN:
                return StoredType.FROZEN;
            case ELSE:
                return StoredType.ELSE;
            default:
                throw new IllegalArgumentException();
        }
    }

    static ViewCategory getByIndex(int index){
        switch (index){
            case 0:
                return ViewCategory.OVERDUE;
            case 1:
                return ViewCategory.COLD;
            case 2:
                return ViewCategory.FROZEN;
            case 3:
                return ViewCategory.ELSE;
            default:
                throw new IllegalArgumentException();
        }
    }
}
