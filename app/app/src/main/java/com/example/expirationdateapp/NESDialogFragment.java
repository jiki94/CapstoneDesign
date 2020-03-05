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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

public class NESDialogFragment extends DialogFragment {
    private String defaultName;
    private String defaultExpiryDate;
    private StoredType defaultStoredType;

    private boolean usingName;
    private boolean usingExpiryDate;
    private boolean usingStoredPlace;

    private View body;
    private NoticeDialogListener listener;

    static class Builder{
        private String defaultName;
        private String defaultExpiryDate;
        private StoredType defaultStoredType;

        private boolean usingName;
        private boolean usingExpiryDate;
        private boolean usingStoredPlace;

        public Builder(){
            defaultName = null;
            defaultExpiryDate = null;
            defaultStoredType = null;

            usingName = true;
            usingExpiryDate = true;
            usingStoredPlace = true;
        }

        public NESDialogFragment build(){
            return new NESDialogFragment(defaultName, defaultExpiryDate, defaultStoredType, usingName, usingExpiryDate, usingStoredPlace);
        }

        public Builder setDefaultName(String defaultName){
            this.defaultName = defaultName;
            return this;
        }

        public Builder setDefaultExpiryDate(String defaultExpiryDate){
            this.defaultExpiryDate = defaultExpiryDate;
            return this;
        }

        public Builder setDefaultStoredType(StoredType defaultStoredType){
            this.defaultStoredType = defaultStoredType;
            return this;
        }

        public Builder setUsingName(boolean usingName){
            this.usingName = usingName;
            if (!this.usingName)
                this.defaultName = null;

            return this;
        }

        public Builder setUsingExpiryDate(boolean usingExpiryDate){
            this.usingExpiryDate = usingExpiryDate;
            if (!this.usingExpiryDate)
                this.defaultExpiryDate = null;

            return this;
        }

        public Builder setUsingStoredPlace(boolean usingStoredPlace){
            this.usingStoredPlace = usingStoredPlace;
            if (!this.usingStoredPlace)
                this.defaultStoredType = null;

            return this;
        }
    }

    private NESDialogFragment(String defaultName, String defaultExpiryDate, StoredType defaultStoredType,
                              boolean usingName, boolean usingExpiryDate, boolean usingStoredPlace){
        this.defaultName = defaultName;
        this.defaultExpiryDate = defaultExpiryDate;
        this.defaultStoredType = defaultStoredType;
        this.usingName = usingName;
        this.usingExpiryDate = usingExpiryDate;
        this.usingStoredPlace = usingStoredPlace;
    }

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
        if (!usingName) {
            body.findViewById(R.id.nesDialog_group_product_name).setVisibility(View.GONE);
        }else{
            if (defaultName != null){
                EditText nameEdit = body.findViewById(R.id.nesDialog_edittext_product_name);
                nameEdit.setText(defaultName);
            }
        }

        if (!usingExpiryDate) {
            body.findViewById(R.id.nesDialog_group_expiry_date).setVisibility(View.GONE);
        }else{
            if (defaultExpiryDate != null){
                EditText expiryEdit = body.findViewById(R.id.nesDialog_edittext_expiry_date);
                expiryEdit.setText(defaultExpiryDate);
            }
        }

        if (!usingStoredPlace) {
            body.findViewById(R.id.nesDialog_radio_group_stored).setVisibility(View.GONE);
        }else{
            if (defaultStoredType != null){
                RadioGroup storedGroup = body.findViewById(R.id.nesDialog_radio_group_stored);
                storedGroup.check(getIdFromStoredType(defaultStoredType));
            }
        }

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
                String name = null;
                String expiryDate = null;
                StoredType stored = null;

                if (usingName) {
                    EditText nameEditText = body.findViewById(R.id.nesDialog_edittext_product_name);
                    name = nameEditText.getText().toString();

                    if (name.isEmpty()) {
                        Snackbar.make(body, R.string.snackbar_empty_name, Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (usingExpiryDate){
                    EditText expiryEditText = body.findViewById(R.id.nesDialog_edittext_expiry_date);
                    expiryDate = expiryEditText.getText().toString();

                    if (expiryDate.isEmpty()){
                        Snackbar.make(body, R.string.snackbar_empty_expiry_date, Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (usingStoredPlace) {
                    RadioGroup radioGroup = body.findViewById(R.id.nesDialog_radio_group_stored);
                    stored = getStoredTypeFromId(radioGroup.getCheckedRadioButtonId());

                    if (stored == null) {
                        Snackbar.make(body, R.string.snackbar_empty_stored, Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                }

                listener.onDialogPositiveClick(name, expiryDate, stored);
                dismiss();
            }
        });
    }

    public interface NoticeDialogListener{
        void onDialogPositiveClick(String name, String expiryDate, StoredType storedType);
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

    private int getIdFromStoredType(StoredType stored){
        switch (stored){
            case COLD:
                return R.id.nesDialog_radio_button_cold;
            case FROZEN:
                return R.id.nesDialog_radio_button_frozen;
            case ELSE:
                return R.id.nesDialog_radio_button_else;
            default:
                throw new RuntimeException("Unsupported id for " + StoredType.class.getName());
        }
    }
}
