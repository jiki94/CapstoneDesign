package com.example.expirationdateapp.viewing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.LocalDateConverter;
import com.example.expirationdateapp.db.Product;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 보기 Fragment 에서 사용
public class ViewRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static private final int NORMAL_TYPE = 0;
    static private final int OVERDUE_TYPE = 1;

    private Context context;
    private List<Product> data;
    private List<Product> filteredData;
    private DBRelatedListener listener;

    private String filterString;
    private SortingType sortFlag;

    class NormalViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView expiryDate;
        private Button delete;
        private ImageView imminentExpiry;

        NormalViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.viewItemNormal_text_name);
            expiryDate = itemView.findViewById(R.id.viewItemNormal_text_expiry_date);
            delete = itemView.findViewById(R.id.viewItemNormal_button_del);
            imminentExpiry = itemView.findViewById(R.id.viewItemNormal_imminentExpiry);
        }
    }

    class OverdueViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView stored;
        private TextView expiryDate;
        private TextView daysPast;
        private Button delete;

        OverdueViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.viewItemOverdue_text_name);
            stored = itemView.findViewById(R.id.viewItemOverdue_text_stored);
            expiryDate = itemView.findViewById(R.id.viewItemOverdue_text_expiry_date);
            daysPast = itemView.findViewById(R.id.viewItemOverdue_text_past_date);
            delete = itemView.findViewById(R.id.viewItemOverdue_button_del);
        }
    }

    public ViewRecyclerViewAdapter(Context context, List<Product> data, DBRelatedListener listener) {
        this.context = context;
        this.data = data;
        this.filterString = null;
        this.sortFlag = SortingType.getDefaultSortFlag();
        this.listener = listener;

        filter();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        RecyclerView.ViewHolder viewholder;
        if (viewType == NORMAL_TYPE) {
            view = inflater.inflate(R.layout.item_view_normal, parent, false);
            viewholder= new NormalViewHolder(view);
        }else if (viewType == OVERDUE_TYPE){
            view = inflater.inflate(R.layout.item_view_overdue, parent, false);
            viewholder= new OverdueViewHolder(view);
        }else{
            throw new IllegalArgumentException();
        }
        return viewholder;
    }

    @Override
    public int getItemViewType(int position) {
        Product datum = filteredData.get(position);
        if (datum.expiryDate.compareTo(LocalDate.now()) >= 0){
            return NORMAL_TYPE;
        }else
            return OVERDUE_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final Product datum = filteredData.get(position);

        switch (viewHolder.getItemViewType()){
            case NORMAL_TYPE:
                NormalViewHolder nHolder = (NormalViewHolder) viewHolder;
                nHolder.name.setText(datum.name);
                nHolder.expiryDate.setText(LocalDateConverter.localDateToString(datum.expiryDate));

                nHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDeletedClicked(datum);
                    }
                });

                if (datum.getExpiryDateInDays() <= 3) {
                    nHolder.imminentExpiry.setVisibility(View.VISIBLE);
                }else {
                    nHolder.imminentExpiry.setVisibility(View.GONE);
                }
                break;
            case OVERDUE_TYPE:
                OverdueViewHolder oHolder = (OverdueViewHolder) viewHolder;
                oHolder.name.setText(datum.name);
                oHolder.stored.setText(datum.stored.getStringId());
                oHolder.expiryDate.setText(LocalDateConverter.localDateToString(datum.expiryDate));
                oHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDeletedClicked(datum);
                    }
                });

                Period period = Period.between(datum.expiryDate, LocalDate.now());
                int pasted = period.getDays();
                oHolder.daysPast.setText(String.valueOf(pasted));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    void changeData(List<Product> newData){
        data = newData;
        filter();
    }

    void setFilterString(String filterString){
        this.filterString = filterString;
        filter();
    }

    void setSortFlag(SortingType newSortFlag){
        if (sortFlag != newSortFlag){
            List<Product> newData = new ArrayList<>(filteredData);
            if (sortFlag.isConjugate(newSortFlag)){
                Collections.reverse(newData);
            }else{
                Collections.sort(newData, newSortFlag.getAssociatedComparator());
            }

            sortFlag = newSortFlag;
            changeShowingData(filteredData, newData);
        }
    }

    // filter, changeShowingData 사용 어려움
    // 한눈에 보이지 않음
    // TODO: Refactor this later
    private void filter(){
        List<Product> newData;
        if (filterString == null || filterString.isEmpty()){
            newData = data;
        }else{
            newData = new ArrayList<Product>();
            for (Product p : data){
                if (p.name.contains(filterString))
                    newData.add(p);
            }
        }

        Collections.sort(newData, sortFlag.getAssociatedComparator());
        changeShowingData(filteredData, newData);
    }

    private void changeShowingData(List<Product> oldData, List<Product> newData){
        if (oldData == null)
            oldData = new ArrayList<>();
        if (newData == null)
            newData = new ArrayList<>();

        ViewRecyclerDiffUtilCallBack diffUtilCallBack = new ViewRecyclerDiffUtilCallBack(oldData, newData);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtilCallBack, true);

        filteredData = newData;
        result.dispatchUpdatesTo(this);
    }

    public interface DBRelatedListener {
        void onDeletedClicked(Product clicked);
    }
}
