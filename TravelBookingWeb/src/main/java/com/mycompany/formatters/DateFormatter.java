
package com.mycompany.formatters;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.springframework.format.Formatter;

public class DateFormatter implements Formatter<Date> {

    @Override
    public String print(Date date, Locale locale) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(date);
    }

    @Override
    public Date parse(String value, Locale locale) throws ParseException {
        if (value == null || value.isBlank()) {
            return null;
        }

        if (value.length() == 10) {
            return java.sql.Date.valueOf(value);
        }

        String normalized = value.replace("T", " ");
        if (normalized.length() == 16) {
            normalized += ":00";
        }
        return Timestamp.valueOf(normalized);
    }
}
