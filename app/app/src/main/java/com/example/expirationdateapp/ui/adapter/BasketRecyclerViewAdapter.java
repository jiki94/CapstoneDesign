package com.example.expirationdateapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirationdateapp.db.Product;
import com.example.expirationdateapp.R;

import java.util.List;

// 장바구니 RecyclerView 에서 사용
public class BasketRecyclerViewAdapter extends RecyclerView.Adapter<BasketRecyclerViewAdapter.BasketViewHolder> {
    private Context context;
    private List<Product> data;
    private DBRelatedListener listener;

    class BasketViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView expiryDate;
        private TextView stored;
        private Button del;

        BasketViewHolder(View view){
            super(view);

            name = view.findViewById(R.id.basketItem_text_name);
            expiryDate = view.findViewById(R.id.basketItem_text_expiry_date);
            stored = view.findViewById(R.id.basketItem_text_stored);
            del = view.findViewById(R.id.basketItem_button_del);
        }
    }

    public BasketRecyclerViewAdapter(Context context, List<Product> data, DBRelatedListener listener){
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_basket, parent, false);
        return new BasketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder holder, int position) {
        final Product item = data.get(position);
        holder.name.setText(item.name);
        holder.expiryDate.setText(item.expiryDate);
        holder.stored.setText(item.stored.getStringId());

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeletedClicked(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface DBRelatedListener {
        void onDeletedClicked(Product clicked);
    }

    public List<Product> getData(){
        return data;
    }

    public void setData(List<Product> newData){
        BasketDiffUtilCallBack callBack = new BasketDiffUtilCallBack(data, newData);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callBack, true);

        data = newData;
        result.dispatchUpdatesTo(this);
    }
}
