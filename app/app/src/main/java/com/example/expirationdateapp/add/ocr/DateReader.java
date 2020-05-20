package com.example.expirationdateapp.add.ocr;

import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateReader {
    static String [] ymdPatStr =  {"(\\d{4})\\s*[/\\.\\ub144]\\s*(\\d{1,2})\\s*[/\\.\\uc6d4]\\s*(\\d{1,2})\\s*\\uc77c?",
            "(\\d{2})\\s*[/\\.\\ub144]\\s*(\\d{1,2})\\s*[/\\.\\uc6d4]\\s*(\\d{1,2})\\s*\\uc77c?",
            "(\\d{4})(\\d{2})(\\d{2})"};
    static String [] mdPatStr = {"(\\d{1,2})\\s*[/\\.\\uc6d4]\\s*(\\d{1,2})\\s*\\uc77c?"};
    static Pattern [] ymdPat;
    static Pattern [] mdPat;
    static {
        ymdPat = new Pattern[ymdPatStr.length];
        for (int i = 0; i < ymdPatStr.length; i++)
            ymdPat[i] = Pattern.compile(ymdPatStr[i]);

        mdPat = new Pattern[mdPatStr.length];
        for (int i = 0; i < mdPatStr.length; i++)
            mdPat[i] = Pattern.compile(mdPatStr[i]);
    }

    static SortedSet<LocalDate> readFromString(String input){
        input = input.replaceAll("\\s", "");
        SortedSet<LocalDate> dates = new TreeSet<>();
        for (Pattern pat : ymdPat){
            Matcher m = pat.matcher(input);
            while (m.find()){
                try {
                    int year = Integer.parseInt(m.group(1));
                    int month = Integer.parseInt(m.group(2));
                    int day = Integer.parseInt(m.group(3));

                    if (year < 100) {
                        year += 2000;
                    }

                    LocalDate date = LocalDate.of(year, month, day);
                    dates.add(date);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("This should never execute unless some problem in regex lib.");
                } catch (DateTimeException e) {
                    // 날짜 형식이지만 실제 날짜가 아님
                }
            }
        }

        for (Pattern pat : mdPat){
            Matcher m = pat.matcher(input);
            while (m.find()){
                try {
                    int month = Integer.parseInt(m.group(1));
                    int day = Integer.parseInt(m.group(2));

                    LocalDate now = LocalDate.now();
                    int year = now.getYear();
                    LocalDate date = LocalDate.of(year, month, day);
                    if (now.isAfter(date))
                        date = date.plusYears(1);

                    dates.add(date);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("This should never execute unless some problem in regex lib.");
                } catch (DateTimeException e) {
                    // 날짜 형식이지만 실제 날짜가 아님
                }
            }
        }

        return dates;
    }
}
