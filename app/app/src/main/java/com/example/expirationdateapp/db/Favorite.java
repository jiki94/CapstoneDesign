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
    @PrimaryKey
    @NonNull
    public String name;

    @NonNull
    @TypeConverters(StoredTypeConverter.class)
    public StoredType stored;

    public Favorite(String name, StoredType stored){
        this.name = name;
        this.stored = stored;
    }

    @Override
    @NonNull
    public String toString(){
        return name + " " + stored.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, stored);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;

        Favorite other = (Favorite) obj;
        return name.equals(other.name) && stored == other.stored;
    }
}
