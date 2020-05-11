package com.example.expirationdateapp.recipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.expirationdateapp.db.RecipeInfo;

import java.util.List;

class RecipeListDiffUtilCallBack extends DiffUtil.Callback {
    @NonNull private List<RecipeInfo> oldList;
    @NonNull private List<RecipeInfo> newList;

    public RecipeListDiffUtilCallBack(@NonNull List<RecipeInfo> oldList, @NonNull List<RecipeInfo> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).recipeCode == newList.get(newItemPosition).recipeCode;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false; // 레시피 정보를 각자 업데이트 안하니까 업데이트 될때는 한번에 하니까
    }
}
