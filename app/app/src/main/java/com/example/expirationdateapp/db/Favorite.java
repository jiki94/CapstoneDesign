package com.example.expirationdateapp.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Objects;

// 즐겨찾기를 나타내는 클래스
@Entity
public class Favorite {
    public static final int BASIC_DEFAULT_ED = 7;

    @PrimaryKey
    @NonNull
    public String name;

    @NonNull
    @TypeConverters(StoredTypeConverter.class)
    public StoredType stored;

    public int defaultED;
    public boolean usingDefaultED;

    public Favorite(String name, StoredType stored, int defaultED, boolean usingDefaultED){
        this.name = name;
        this.stored = stored;
        this.defaultED = defaultED;
        this.usingDefaultED = usingDefaultED;
    }

    @Override
    @NonNull
    public String toString(){
        return name + " " + stored.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, stored, defaultED, usingDefaultED);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;

        Favorite other = (Favorite) obj;
        return name.equals(other.name) && stored == other.stored && defaultED == other.defaultED &&
                usingDefaultED == other.usingDefaultED;
    }
}
