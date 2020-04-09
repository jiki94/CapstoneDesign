package com.example.expirationdateapp.add;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import java.util.EnumSet;
import java.util.List;

import static com.example.expirationdateapp.add.AddFragment.REQUEST_CODE_OCR_ACT;
import static com.example.expirationdateapp.add.AddFragment.REQUEST_CODE_STT_ACT;

// 즐겨찾기 RecyclerView 에서 사용
public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.FavoriteViewHolder> {
    @NonNull private Context context;
    @NonNull private List<Favorite> data;
    @NonNull private DBRelatedListener dbRelatedListener;
    @NonNull private AddFragmentDialogManager dialogManager;
    @NonNull private Fragment fragment;

    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private Button del;
        private Button ocr;
        private Button stt;
        private Button manual;
        private RadioGroup stored;
        private ToggleButton useDefaultED;
        private EditText defaultED;

        FavoriteViewHolder(View view){
            super(view);

            name = view.findViewById(R.id.favoriteItem_text_name);
            del = view.findViewById(R.id.favoriteItem_button_del);
            ocr = view.findViewById(R.id.favoriteItem_button_ocr);
            stt = view.findViewById(R.id.favoriteItem_button_stt);
            manual = view.findViewById(R.id.favoriteItem_button_manual);
            stored = view.findViewById(R.id.favoriteItem_radioGroup_stored);
            useDefaultED = view.findViewById(R.id.favoriteItem_toggle_expiry_date);
            defaultED = view.findViewById(R.id.favoriteItem_edit_expiry_date);
        }
    }

    enum PayloadEnum{
        STORED_CHANGED, DEFAULT_ED_CHANGED
    }

    static class Payload{
        private EnumSet<PayloadEnum> flags;

        Payload(){
            flags = EnumSet.noneOf(PayloadEnum.class);
        }

        void set(PayloadEnum setting){
            flags.add(setting);
        }
    }

    FavoriteRecyclerViewAdapter(@NonNull Context context, @NonNull ArrayList<Favorite> data,
                                       @NonNull DBRelatedListener listener, @NonNull AddFragmentDialogManager dialogManager,
                                       @NonNull Fragment fragment){
        this.context = context;
        this.data = data;
        this.dbRelatedListener = listener;
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
            if (payload.flags.contains(PayloadEnum.STORED_CHANGED))
                updateHolderStoredType(holder, datum.stored);
            if (payload.flags.contains(PayloadEnum.DEFAULT_ED_CHANGED))
                updateHolderED(holder, datum.defaultED, datum.usingDefaultED);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteViewHolder holder, final int position) {
        final Favorite datum = data.get(position);

        // 뷰에 리스너들 세팅
        holder.useDefaultED.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                datum.usingDefaultED = isChecked;
                dbRelatedListener.onDefaultExpiryDateChanged(datum);
            }
        });

        holder.defaultED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                datum.defaultED = Integer.decode(s.toString());
                dbRelatedListener.onDefaultExpiryDateChanged(datum);
            }
        });

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRelatedListener.onDeletedClicked(datum);
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
                DialogFragment dialog = dialogManager.getAddManualDialogFragment(datum.name,
                        datum.defaultED, datum.usingDefaultED, datum.stored);
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

                dbRelatedListener.onStoredChanged(datum);
            }
        });


        holder.name.setText(datum.name);
        updateHolderStoredType(holder, datum.stored);
        updateHolderED(holder, datum.defaultED, datum.usingDefaultED);
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

    private static void updateHolderED(@NonNull FavoriteViewHolder holder, int defaultED, boolean usingDefaultED){
        holder.useDefaultED.setChecked(usingDefaultED);
        holder.defaultED.setEnabled(usingDefaultED);
        holder.defaultED.setText(String.valueOf(defaultED));
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
        void onDefaultExpiryDateChanged(Favorite changedFavorite);
    }
}
