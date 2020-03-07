package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class FavoriteDiffUtilCallBack extends DiffUtil.Callback {
    @NonNull List<Favorite> oldData;
    @NonNull List<Favorite> newData;

    public FavoriteDiffUtilCallBack(@NonNull  List<Favorite> oldData, @NonNull List<Favorite> newData){
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Favorite oldOne = oldData.get(oldItemPosition);
        Favorite newOne = newData.get(newItemPosition);
        return oldOne.name.equals(newOne.name);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Favorite newOne = newData.get(newItemPosition);
        return FavoriteRecyclerViewAdapter.Payload.STORED_CHANGED;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Favorite oldOne = oldData.get(oldItemPosition);
        Favorite newOne = newData.get(newItemPosition);
        return oldOne.equals(newOne);
    }
}
