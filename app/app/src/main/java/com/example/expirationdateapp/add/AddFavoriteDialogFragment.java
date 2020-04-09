package com.example.expirationdateapp.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.Favorite;
import com.example.expirationdateapp.db.LocalDateConverter;
import com.example.expirationdateapp.db.StoredType;
import com.google.android.material.snackbar.Snackbar;

import org.threeten.bp.LocalDate;

// 즐겨찾기 추가 다이얼로그
public class AddFavoriteDialogFragment extends DialogFragment {
    private EditText defaultExpiryDate;
    private View body;
    private AddFavoriteResult listener;

    private boolean usingDefaultExpiryDate;

    AddFavoriteDialogFragment(){

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (AddFavoriteResult) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException(getTargetFragment() +
                    " must implement " + NESDialogFragment.NoticeDialogListener.class.getName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 다이얼로그 모양이랑 기본 값 설정
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        body = inflater.inflate(R.layout.dialog_add_favorite, null);

        defaultExpiryDate = body.findViewById(R.id.addFavDialog_edittext_day_after);
        ToggleButton toggleButton = body.findViewById(R.id.addFavDialog_toggle_default_expiry_date);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                usingDefaultExpiryDate = isChecked;
                defaultExpiryDate.setEnabled(usingDefaultExpiryDate);
            }
        });

        toggleButton.setChecked(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(body);

        // OnClickListener를 null로 한 이유는 입력이 비었을 경우 처리할려고
        builder.setPositiveButton(R.string.button_add, null);

        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onAddFavoriteNegativeClick();
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        // 다이얼로그 추가 버튼 설정
        AlertDialog alertDialog = (AlertDialog) getDialog();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = null;
                StoredType stored = null;

                EditText nameEditText = body.findViewById(R.id.addFavDialog_edittext_product_name);
                name = nameEditText.getText().toString();

                if (name.isEmpty()) {
                    Snackbar.make(body, R.string.snackbar_empty_name, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                int defaultDate = Favorite.BASIC_DEFAULT_ED;
                if (usingDefaultExpiryDate){
                    String date = defaultExpiryDate.getText().toString();
                    if (date.isEmpty()){
                        Snackbar.make(body, R.string.snackbar_empty_expiry_date, Snackbar.LENGTH_SHORT).show();
                        return;
                    }else{
                        defaultDate = Integer.decode(date);
                    }
                }

                RadioGroup radioGroup = body.findViewById(R.id.addFavDialog_radio_group_stored);
                stored = getStoredTypeFromId(radioGroup.getCheckedRadioButtonId());

                if (stored == null) {
                    Snackbar.make(body, R.string.snackbar_empty_stored, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // make interface for favorite
                listener.onAddFavoritePositiveClick(new Favorite(name, stored, defaultDate, usingDefaultExpiryDate));
                dismiss();
            }
        });
    }

    // 호출한 Fragment 에서 다이얼로그 결과 받는 인터페이스
    public interface AddFavoriteResult{
        void onAddFavoritePositiveClick(Favorite newFavorite);
        void onAddFavoriteNegativeClick();
    }

    private StoredType getStoredTypeFromId(int id){
        if (id == R.id.addFavDialog_radio_button_cold)
            return StoredType.COLD;
        if (id == R.id.addFavDialog_radio_button_frozen)
            return StoredType.FROZEN;
        if (id == R.id.addFavDialog_radio_button_else)
            return StoredType.ELSE;
        return null;
    }

    private int getIdFromStoredType(StoredType stored){
        switch (stored){
            case COLD:
                return R.id.addFavDialog_radio_button_cold;
            case FROZEN:
                return R.id.addFavDialog_radio_button_frozen;
            case ELSE:
                return R.id.addFavDialog_radio_button_else;
            default:
                throw new RuntimeException("Unsupported id for " + StoredType.class.getName());
        }
    }
}
