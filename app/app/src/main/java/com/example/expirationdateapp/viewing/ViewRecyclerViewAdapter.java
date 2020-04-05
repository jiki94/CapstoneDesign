package com.example.expirationdateapp.viewing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.LocalDateConverter;
import com.example.expirationdateapp.db.Product;

import java.util.List;

// 보기 Fragment 에서 사용
public class ViewRecyclerViewAdapter extends RecyclerView.Adapter<ViewRecyclerViewAdapter.CustomViewHolder> {
    private Context context;
    private List<Product> data;
    private DBRelatedListener listener;

    class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView expiryDate;
        private Button delete;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.viewItem_text_name);
            expiryDate = itemView.findViewById(R.id.viewItem_text_expiry_date);
            delete = itemView.findViewById(R.id.viewItem_button_del);
        }
    }

    public ViewRecyclerViewAdapter(Context context, List<Product> data, DBRelatedListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final Product datum = data.get(position);

        holder.name.setText(datum.name);
        holder.expiryDate.setText(LocalDateConverter.localDateToString(datum.expiryDate));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeletedClicked(datum);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void changeData(List<Product> newData){
        ViewRecyclerDiffUtilCallBack diffUtilCallBack = new ViewRecyclerDiffUtilCallBack(data, newData);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtilCallBack, true);

        data = newData;
        result.dispatchUpdatesTo(this);
    }

    public interface DBRelatedListener {
        void onDeletedClicked(Product clicked);
    }
}
