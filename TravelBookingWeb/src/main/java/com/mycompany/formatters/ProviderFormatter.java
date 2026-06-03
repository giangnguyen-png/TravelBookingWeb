
package com.mycompany.formatters;

import com.mycompany.pojo.ProviderProfiles;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public class ProviderFormatter implements Formatter<ProviderProfiles> {

    @Override
    public String print(ProviderProfiles provider, Locale locale) {
        return provider != null && provider.getId() != null ? String.valueOf(provider.getId()) : "";
    }

    @Override
    public ProviderProfiles parse(String providerId, Locale locale) throws ParseException {
        ProviderProfiles provider = new ProviderProfiles();
        provider.setId(FormatterUtils.parseId(providerId));
        return provider;
    }
}
