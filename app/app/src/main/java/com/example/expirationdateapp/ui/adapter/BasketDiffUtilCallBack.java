package com.example.expirationdateapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.expirationdateapp.db.Product;

import java.util.List;

// 장바구니 RecyclerView 에서 사용
public class BasketDiffUtilCallBack extends DiffUtil.Callback{
    @NonNull private List<Product> oldData;
    @NonNull private List<Product> newData;

    public BasketDiffUtilCallBack(@NonNull  List<Product> oldData, @NonNull List<Product> newData){
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
        Product oldItem = oldData.get(oldItemPosition);
        Product newItem = newData.get(newItemPosition);
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Product oldItem = oldData.get(oldItemPosition);
        Product newItem = newData.get(newItemPosition);
        return oldItem.equals(newItem);
    }
}
