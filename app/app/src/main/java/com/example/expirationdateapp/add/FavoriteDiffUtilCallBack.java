package com.example.expirationdateapp.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.expirationdateapp.db.Favorite;

import java.util.List;

// 즐겨찾기 RecyclerView 에서 사용
public class FavoriteDiffUtilCallBack extends DiffUtil.Callback {
    @NonNull private List<Favorite> oldData;
    @NonNull private List<Favorite> newData;

    FavoriteDiffUtilCallBack(@NonNull  List<Favorite> oldData, @NonNull List<Favorite> newData){
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

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Favorite oldOne = oldData.get(oldItemPosition);
        Favorite newOne = newData.get(newItemPosition);
        return oldOne.equals(newOne);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Favorite newOne = newData.get(newItemPosition);
        return FavoriteRecyclerViewAdapter.Payload.STORED_CHANGED;
    }


}
