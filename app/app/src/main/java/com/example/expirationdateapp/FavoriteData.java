package com.example.expirationdateapp;

public class FavoriteData {
    public String name;
    public StoredType stored;

    public FavoriteData(String name, StoredType stored){
        this.name = name;
        this.stored = stored;
    }

    @Override
    public String toString(){
        return name + " " + stored.toString();
    }
}
