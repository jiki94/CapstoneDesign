package com.example.expirationdateapp.add;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirationdateapp.add.ocr.OcrActivity;
import com.example.expirationdateapp.add.stt.SttActivity;
import com.example.expirationdateapp.db.Favorite;
import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.StoredType;

import java.util.ArrayList;
import java.util.List;

import static com.example.expirationdateapp.add.AddFragment.REQUEST_CODE_OCR_ACT;
import static com.example.expirationdateapp.add.AddFragment.REQUEST_CODE_STT_ACT;

// 즐겨찾기 RecyclerView 에서 사용
public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.FavoriteViewHolder> {
    @NonNull private Context context;
    @NonNull private List<Favorite> data;
    @NonNull private DBRelatedListener deletedListener;
    @NonNull private AddFragmentDialogManager dialogManager;
    @NonNull private Fragment fragment;

    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private Button del;
        private Button ocr;
        private Button stt;
        private Button manual;
        private RadioGroup stored;

        FavoriteViewHolder(View view){
            super(view);

            name = view.findViewById(R.id.favoriteItem_text_name);
            del = view.findViewById(R.id.favoriteItem_button_del);
            ocr = view.findViewById(R.id.favoriteItem_button_ocr);
            stt = view.findViewById(R.id.favoriteItem_button_stt);
            manual = view.findViewById(R.id.favoriteItem_button_manual);
            stored = view.findViewById(R.id.favoriteItem_radioGroup_stored);
        }
    }

    public enum Payload{
        STORED_CHANGED
    }

    FavoriteRecyclerViewAdapter(@NonNull Context context, @NonNull ArrayList<Favorite> data,
                                       @NonNull DBRelatedListener listener, @NonNull AddFragmentDialogManager dialogManager,
                                       @NonNull Fragment fragment){
        this.context = context;
        this.data = data;
        this.deletedListener = listener;
        this.dialogManager = dialogManager;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View createdView = layoutInflater.inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(createdView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteViewHolder holder, final int position,
                                 @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }

        final Favorite datum = data.get(position);

        for (Object obj : payloads) {
            Payload payload = (Payload) obj;
            if (payload == Payload.STORED_CHANGED){
                updateHolderStoredType(holder, datum.stored);
            }else{
                throw new IllegalArgumentException(FavoriteRecyclerViewAdapter.class.getName() + " does not support " + payload.toString());
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteViewHolder holder, final int position) {
        final Favorite datum = data.get(position);

        holder.name.setText(datum.name);

        updateHolderStoredType(holder, datum.stored);

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletedListener.onDeletedClicked(datum);
            }
        });

        holder.ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "OCR: " + datum.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, OcrActivity.class);
                intent.putExtra(context.getString(R.string.key_get_type), GetType.EXPIRY_DATE);
                intent.putExtra(context.getString(R.string.key_name_data), datum.name);
                intent.putExtra(context.getString(R.string.key_stored_type), datum.stored);
                fragment.startActivityForResult(intent, REQUEST_CODE_OCR_ACT);
            }
        });

        holder.stt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "STT: " + datum.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, SttActivity.class);
                intent.putExtra(context.getString(R.string.key_get_type), GetType.EXPIRY_DATE);
                intent.putExtra(context.getString(R.string.key_name_data), datum.name);
                intent.putExtra(context.getString(R.string.key_stored_type), datum.stored);
                fragment.startActivityForResult(intent, REQUEST_CODE_STT_ACT);
            }
        });

        holder.manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Manual: " + datum.toString(), Toast.LENGTH_SHORT).show();
                DialogFragment dialog = dialogManager.getAddManualDialogFragment(datum.name, null, datum.stored);
                dialog.show(dialogManager.getFragmentManager(), "Recyclcer_item_add_manual");
            }
        });

        holder.stored.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.favoriteItem_radio_button_cold){
                    datum.stored = StoredType.COLD;
                }else if (checkedId == R.id.favoriteItem_radio_button_frozen){
                    datum.stored = StoredType.FROZEN;
                }else if (checkedId == R.id.favoriteItem_radio_button_else){
                    datum.stored = StoredType.ELSE;
                }else{
                    throw new RuntimeException("There is no radio button corresponding to id");
                }

                deletedListener.onStoredChanged(datum);
            }
        });
    }

    private static void updateHolderStoredType(@NonNull FavoriteViewHolder holder, @NonNull StoredType stored){
        switch (stored){
            case COLD:
                holder.stored.check(R.id.favoriteItem_radio_button_cold);
                break;
            case FROZEN:
                holder.stored.check(R.id.favoriteItem_radio_button_frozen);
                break;
            case ELSE:
                holder.stored.check(R.id.favoriteItem_radio_button_else);
                break;
            default:
                throw new RuntimeException(StoredType.class.getName() + " does not support " + stored.toString());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void changeData(@NonNull List<Favorite> newData){
        FavoriteDiffUtilCallBack callBack = new FavoriteDiffUtilCallBack(data, newData);

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callBack, true);
        data = newData;
        result.dispatchUpdatesTo(this);
    }

    public interface DBRelatedListener {
        void onDeletedClicked(Favorite clickedFavorite);
        void onStoredChanged(Favorite changedFavorite);
    }
}
