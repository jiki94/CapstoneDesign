package com.example.expirationdateapp.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.RecipeInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListRecyclerViewAdapter extends RecyclerView.Adapter<RecipeListRecyclerViewAdapter.ViewHolder> {
    @NonNull private Context context;
    @NonNull private List<RecipeInfo> data;
    private ItemClickedListener listener;

    class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        ImageView mainImg;
        TextView name;
        TextView desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            mainImg = itemView.findViewById(R.id.recipeItem_image_main);
            name = itemView.findViewById(R.id.recipeItem_text_name);
            desc = itemView.findViewById(R.id.recipeItem_text_desc);
        }
    }

    public RecipeListRecyclerViewAdapter(@NonNull Context context, @NonNull List<RecipeInfo> data, ItemClickedListener listener){
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_recipe_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeInfo datum = data.get(position);
        holder.name.setText(datum.recipeName);
        holder.desc.setText(datum.recipeBasicDesc);

        // placeholder 에 해당하는 사진 추가
        Picasso.get()
                .load(datum.mainImgUrl)
                .placeholder(R.drawable.basket)
                .into(holder.mainImg);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClicked(datum);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void changeData(@NonNull List<RecipeInfo> newData){
        data = newData;
        notifyDataSetChanged();
    }

    public interface ItemClickedListener {
        void onItemClicked(RecipeInfo clickedRecipe);
    }
}
