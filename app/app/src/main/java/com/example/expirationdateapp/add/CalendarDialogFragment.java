package com.example.expirationdateapp.add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.threeten.bp.LocalDate;

public class CalendarDialogFragment extends DialogFragment{
    @NonNull private Context context;
    @NonNull private DatePickerDialog.OnDateSetListener listener;
    private LocalDate date;

    public CalendarDialogFragment(@NonNull Context context, @NonNull DatePickerDialog.OnDateSetListener listener) {
        this.context = context;
        this.listener = listener;
        this.date = null;
    }

    public CalendarDialogFragment(@NonNull Context context, @NonNull DatePickerDialog.OnDateSetListener listener,
                                  LocalDate date) {
        this.context = context;
        this.listener = listener;
        this.date = date;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LocalDate ld;
        if (date == null) {
            ld = LocalDate.now();
        }else{
            ld = date;
        }

        int year = ld.getYear();
        int month = ld.getMonthValue() - 1;
        int day = ld.getDayOfMonth();

        return new DatePickerDialog(context, listener, year, month, day);
    }
}
