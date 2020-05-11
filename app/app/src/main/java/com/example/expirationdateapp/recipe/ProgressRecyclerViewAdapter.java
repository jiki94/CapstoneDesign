package com.example.expirationdateapp.recipe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.RecipeIngredient;
import com.example.expirationdateapp.db.RecipeProgress;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProgressRecyclerViewAdapter extends RecyclerView.Adapter<ProgressRecyclerViewAdapter.ViewHolder> {
    @NonNull private Context context;
    @NonNull private List<RecipeProgress> data;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView order;
        TextView progressDesc;
        TextView tipDesc;
        ImageView progressImg;
        Group tipGroup;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            order = itemView.findViewById(R.id.progressItem_text_order);
            progressDesc = itemView.findViewById(R.id.progressItem_text_desc);
            tipDesc = itemView.findViewById(R.id.progressItem_text_tip_desc);
            progressImg = itemView.findViewById(R.id.progressItem_img_progress);
            tipGroup = itemView.findViewById(R.id.progressItem_group_tip);
        }
    }

    public ProgressRecyclerViewAdapter(@NonNull Context context) {
        super();
        this.context = context;
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_progress, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.v("RECIPE_TEST", "onBind: position: " + position);
        final RecipeProgress datum = data.get(position);

        holder.order.setText(String.valueOf(datum.recipeOrder));
        holder.progressDesc.setText(datum.recipeDesc);
        if (datum.progressTip == null){
            holder.tipGroup.setVisibility(View.GONE);
        }else{
            holder.tipGroup.setVisibility(View.VISIBLE);
            holder.tipDesc.setText(datum.progressTip);
        }

        if (datum.progressPicture == null){
            holder.progressImg.setVisibility(View.GONE);
        }else{
            holder.progressImg.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(datum.progressPicture)
                    .placeholder(R.drawable.basket)
                    .into(holder.progressImg);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void setData(@NonNull List<RecipeProgress> data){
        this.data = data;
        notifyDataSetChanged();
    }
}
