package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class BasketDiffUtilCallBack extends DiffUtil.Callback{
    @NonNull List<BasketItem> oldData;
    @NonNull List<BasketItem> newData;

    public BasketDiffUtilCallBack(@NonNull  List<BasketItem> oldData, @NonNull List<BasketItem> newData){
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
        BasketItem oldItem = oldData.get(oldItemPosition);
        BasketItem newItem = newData.get(newItemPosition);
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BasketItem oldItem = oldData.get(oldItemPosition);
        BasketItem newItem = newData.get(newItemPosition);
        return oldItem.equals(newItem);
    }
}
