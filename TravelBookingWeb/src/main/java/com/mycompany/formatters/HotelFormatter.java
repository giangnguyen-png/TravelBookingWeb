
package com.mycompany.formatters;

import com.mycompany.pojo.Hotels;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public class HotelFormatter implements Formatter<Hotels> {

    @Override
    public String print(Hotels hotel, Locale locale) {
        return hotel != null && hotel.getId() != null ? String.valueOf(hotel.getId()) : "";
    }

    @Override
    public Hotels parse(String hotelId, Locale locale) throws ParseException {
        Hotels hotel = new Hotels();
        hotel.setId(FormatterUtils.parseId(hotelId));
        return hotel;
    }
}
