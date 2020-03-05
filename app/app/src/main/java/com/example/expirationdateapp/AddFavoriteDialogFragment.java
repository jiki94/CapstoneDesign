package com.example.expirationdateapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

public class AddFavoriteDialogFragment extends DialogFragment {
    private View body;
    private NoticeDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (NoticeDialogListener) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException(getTargetFragment() +
                    " must implement " + NoticeDialogListener.class.getName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        body = inflater.inflate(R.layout.dialog_with_nes, null);
        body.findViewById(R.id.nesDialog_group_expiry_date).setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(body);

        // OnClickListener를 null로 한 이유는 입력이 비었을 경우 처리할려고
        builder.setPositiveButton(R.string.button_add, null);

        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNegativeClick();
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = body.findViewById(R.id.nesDialog_edittext_product_name);
                String name = nameEditText.getText().toString();

                if (name.isEmpty()){
                    Snackbar.make(body, R.string.snackbar_empty_name, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                RadioGroup radioGroup = body.findViewById(R.id.nesDialog_radio_group_stored);
                StoredType stored = getStoredTypeFromId(radioGroup.getCheckedRadioButtonId());

                if (stored == null){
                    Snackbar.make(body, R.string.snackbar_empty_stored, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                listener.onDialogPositiveClick(new FavoriteData(name, stored));
                dismiss();
            }
        });
    }

    public interface NoticeDialogListener{
        void onDialogPositiveClick(FavoriteData newData);
        void onDialogNegativeClick();
    }

    private StoredType getStoredTypeFromId(int id){
        if (id == R.id.nesDialog_radio_button_cold)
            return StoredType.COLD;
        if (id == R.id.nesDialog_radio_button_frozen)
            return StoredType.FROZEN;
        if (id == R.id.nesDialog_radio_button_else)
            return StoredType.ELSE;
        return null;
    }
}
