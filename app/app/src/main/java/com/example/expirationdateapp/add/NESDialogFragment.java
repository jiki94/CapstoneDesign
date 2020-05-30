package com.example.expirationdateapp.add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.LocalDateConverter;
import com.example.expirationdateapp.db.StoredType;
import com.google.android.material.snackbar.Snackbar;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;
import org.threeten.bp.temporal.ChronoUnit;

// 이름, 유통기한, 저장공간 정보를 입력할 수 있는 다이얼로그
public class NESDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private String defaultName;
    private LocalDate defaultExpiryDate;
    private StoredType defaultStoredType;

    // 이름, 유통기한, 저장공간을 사용할 지
    private boolean usingName;
    private boolean usingExpiryDate;
    private boolean usingStoredPlace;

    private LocalDate localDate;

    private View body;
    private TextView expiryText;
    private EditText afterDaysEditText;
    private NoticeDialogListener listener;

    // 다이얼로그 생성 더 쉽
    static class Builder{
        private String defaultName;
        private LocalDate defaultExpiryDate;
        private StoredType defaultStoredType;

        private boolean usingName;
        private boolean usingExpiryDate;
        private boolean usingStoredPlace;

        Builder(){
            defaultName = null;
            defaultExpiryDate = null;
            defaultStoredType = null;

            usingName = true;
            usingExpiryDate = true;
            usingStoredPlace = true;
        }

        NESDialogFragment build(){
            return new NESDialogFragment(defaultName, defaultExpiryDate, defaultStoredType, usingName, usingExpiryDate, usingStoredPlace);
        }

        Builder setDefaultName(String defaultName){
            this.defaultName = defaultName;
            return this;
        }

        Builder setDefaultExpiryDate(LocalDate defaultExpiryDate){
            this.defaultExpiryDate = defaultExpiryDate;
            return this;
        }

        Builder setDefaultStoredType(StoredType defaultStoredType){
            this.defaultStoredType = defaultStoredType;
            return this;
        }

        Builder setUsingName(boolean usingName){
            this.usingName = usingName;
            if (!this.usingName)
                this.defaultName = null;

            return this;
        }

        Builder setUsingExpiryDate(boolean usingExpiryDate){
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

    private NESDialogFragment(String defaultName, LocalDate defaultExpiryDate, StoredType defaultStoredType,
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
        // 다이얼로그 모양이랑 기본 값 설정
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        body = inflater.inflate(R.layout.dialog_with_nes, null);

        expiryText = body.findViewById(R.id.nesDialog_text_expiry_date_show);
        afterDaysEditText = body.findViewById(R.id.nesDialog_edittext_day_after);

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
            afterDaysEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.d("LOCAL_DATE_TEST", "after textChange" + s.toString());
                    String input = s.toString();
                    try {
                        int days = input.isEmpty() ? 0 : Integer.decode(input);
                        setNewLocalDate(LocalDate.now().plusDays(days), true);
                    }catch (NumberFormatException e){
                        // do nothing
                    }
                }
            });

            if (defaultExpiryDate == null)
                defaultExpiryDate = LocalDate.now();
            setNewLocalDate(defaultExpiryDate, false);
        }

        if (!usingStoredPlace) {
            body.findViewById(R.id.nesDialog_radio_group_stored).setVisibility(View.GONE);
        }else{
            if (defaultStoredType != null){
                RadioGroup storedGroup = body.findViewById(R.id.nesDialog_radio_group_stored);
                storedGroup.check(getIdFromStoredType(defaultStoredType));
            }
        }

        final DatePickerDialog.OnDateSetListener target = this;
        final DialogFragment top = this;
        ImageButton imgButton = body.findViewById(R.id.nesDialog_imgButton_calandar);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new CalendarDialogFragment(top.requireContext(), target);
                dialog.show(top.getFragmentManager(), "datePicker");
            }
        });

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

        // 다이얼로그 추가 버튼 설정
        AlertDialog alertDialog = (AlertDialog) getDialog();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = null;
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
                    if (localDate == null){
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

                listener.onDialogPositiveClick(name, localDate, stored);
                dismiss();
            }
        });
    }

    // 호출한 Fragment 에서 다이얼로그 결과 받는 인터페이스
    public interface NoticeDialogListener{
        void onDialogPositiveClick(String name, LocalDate expiryDate, StoredType storedType);
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

    // DatePickerDialog.OnDateSetListener 인터페이스 구현
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        setNewLocalDate(LocalDate.of(year, month + 1, dayOfMonth), false);
    }

    private long calculateDaysAfter(LocalDate givenDate){
        return ChronoUnit.DAYS.between(LocalDate.now(), givenDate);
    }

    private void setNewLocalDate(LocalDate newDate, boolean fromEditText){
        localDate = newDate;
        Log.d("LOCAL_DATE_TEST", "new LocalDate is "  + LocalDateConverter.localDateToString(localDate));
        if (!fromEditText)
            afterDaysEditText.setText(Long.toString(calculateDaysAfter(localDate)));

        expiryText.setText(LocalDateConverter.localDateToString(localDate));
        Log.d("LOCAL_DATE_TEST", LocalDateConverter.localDateToString(localDate));
    }
}
