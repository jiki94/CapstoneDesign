package com.example.expirationdateapp.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.threeten.bp.LocalDate;

public class CalendarDialogFragment extends DialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LocalDate ld = LocalDate.now();
        int year = ld.getYear();
        int month = ld.getMonthValue();
        int day = ld.getDayOfMonth();

        DatePickerDialog.OnDateSetListener listener;
        if (getTargetFragment() instanceof DatePickerDialog.OnDateSetListener){
            listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();
        }else{
            throw new ClassCastException("Must implements " + DatePickerDialog.OnDateSetListener.class.getName());
        }

        return new DatePickerDialog(getTargetFragment().getContext(), listener, year, month, day);
    }
}
