package com.example.expirationdateapp.recipe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.RecipeIngredient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {
    @NonNull private Context context;
    @NonNull private List<RecipeIngredient> data;


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView amount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ingredientItem_name);
            amount = itemView.findViewById(R.id.ingredientItem_amount);
        }
    }

    IngredientRecyclerViewAdapter(@NonNull Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.v("RECIPE_TEST", "onBindViewHolder  position: " + position);
        final RecipeIngredient datum = data.get(position);

        holder.name.setText(datum.ingredientName);
        holder.amount.setText(datum.ingredientAmount);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void setData(@NonNull List<RecipeIngredient> data){
        this.data = data;
        notifyDataSetChanged();
    }
}
