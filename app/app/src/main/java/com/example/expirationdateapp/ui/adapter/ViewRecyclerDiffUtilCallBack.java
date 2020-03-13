package com.example.expirationdateapp.ui.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.example.expirationdateapp.db.Product;

import java.util.List;

// 보기 Fragment 에서 사용
public class ViewRecyclerDiffUtilCallBack extends DiffUtil.Callback {
    private List<Product> oldData;
    private List<Product> newData;

    public ViewRecyclerDiffUtilCallBack(List<Product> oldData, List<Product> newData) {
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
        Product oldOne = oldData.get(oldItemPosition);
        Product newOne = newData.get(newItemPosition);
        return oldOne.id == newOne.id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Product oldOne = oldData.get(oldItemPosition);
        Product newOne = newData.get(newItemPosition);
        return oldOne.id == newOne.id;
    }
}
