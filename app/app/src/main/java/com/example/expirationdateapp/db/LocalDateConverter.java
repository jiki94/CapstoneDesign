package com.example.expirationdateapp.db;

import androidx.room.TypeConverter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

// db 에서 Epoch Day 로 저장(Epoch 값 하지만 Second 대신 Day)
public class LocalDateConverter {
    @TypeConverter
    public static LocalDate longToLocalDate(Long epochDate){
        return epochDate != null ? LocalDate.ofEpochDay(epochDate) : null;
    }

    @TypeConverter
    public static Long localDateToLong(LocalDate localDate){
        return localDate != null ? localDate.toEpochDay() : null;
    }

    public static String localDateToString(LocalDate localDate){
        if (localDate == null)
            return "";
        return localDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
    }
}
