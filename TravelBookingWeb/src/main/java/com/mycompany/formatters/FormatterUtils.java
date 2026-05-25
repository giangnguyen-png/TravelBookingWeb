package com.mycompany.formatters;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class FormatterUtils {

    private static final Pattern ENTITY_ID_PATTERN = Pattern.compile("\\[\\s*id\\s*=\\s*(\\d+)\\s*\\]");

    private FormatterUtils() {
    }

    static Long parseId(String value) throws ParseException {
        if (value == null || value.isBlank()) {
            return null;
        }

        String trimmed = value.trim();
        try {
            return Long.valueOf(trimmed);
        } catch (NumberFormatException ex) {
            Matcher matcher = ENTITY_ID_PATTERN.matcher(trimmed);
            if (matcher.find()) {
                return Long.valueOf(matcher.group(1));
            }

            ParseException parseException = new ParseException("Invalid entity id: " + value, 0);
            parseException.initCause(ex);
            throw parseException;
        }
    }
}
