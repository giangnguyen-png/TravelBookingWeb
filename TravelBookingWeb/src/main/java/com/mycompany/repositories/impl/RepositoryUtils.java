/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repositories.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.hibernate.query.Query;

/**
 *
 * @author nguyen
 */
final class RepositoryUtils {

    private RepositoryUtils() {
    }

    static void paginate(Query<?> query, Map<String, String> params) {
        if (params == null) {
            return;
        }

        int configuredSize = getConfiguredPageSize();
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int size = Math.min(Integer.parseInt(params.getOrDefault("size", String.valueOf(configuredSize))), configuredSize);
        int start = (page - 1) * size;

        query.setFirstResult(start);
        query.setMaxResults(size);
    }

    static Date parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return java.sql.Date.valueOf(value);
        } catch (IllegalArgumentException ex) {
            String normalized = value.replace("T", " ");
            if (normalized.length() == 16) {
                normalized += ":00";
            }
            return Timestamp.valueOf(normalized);
        }
    }

    private static int getConfiguredPageSize() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("configs");
            return Integer.parseInt(bundle.getString("page.size"));
        } catch (MissingResourceException | NumberFormatException ex) {
            return 20;
        }
    }
}
