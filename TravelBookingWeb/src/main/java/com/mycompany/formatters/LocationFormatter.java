/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.formatters;

import com.mycompany.pojo.Locations;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author nguyen
 */
public class LocationFormatter implements Formatter<Locations> {

    @Override
    public String print(Locations location, Locale locale) {
        return location != null && location.getId() != null ? String.valueOf(location.getId()) : "";
    }

    @Override
    public Locations parse(String locationId, Locale locale) throws ParseException {
        Locations location = new Locations();
        location.setId(FormatterUtils.parseId(locationId));
        return location;
    }
}
