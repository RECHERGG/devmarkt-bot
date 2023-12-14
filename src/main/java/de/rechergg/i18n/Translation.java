package de.rechergg.i18n;

import de.rechergg.DevCordBot;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translation {

    private final String resourceBundlePrefix;
    private final ClassLoader classLoader;

    public Translation(String resourceBundlePrefix) {
        this.resourceBundlePrefix = resourceBundlePrefix;
        this.classLoader = getClass().getClassLoader();
    }

    public String get(String key) {
        return get(key, Collections.emptyList());
    }

    public String get(String key, Object... format) {
        return get(key, Locale.ENGLISH, format);
    }

    public String get(String key, Locale locale, Object... format) {
        try {
            ResourceBundle resourceBundle = resourceBundle(locale);

            if (!resourceBundle.containsKey(key)) {
                return key;
            }

            if (format.length == 0) {
                return resourceBundle.getString(key);
            }

            return MessageFormat.format(resourceBundle.getString(key), format);
        } catch (Exception e) {
            return key;
        }
    }

    private ResourceBundle resourceBundle(Locale locale) {
        try {
            return ResourceBundle.getBundle(this.resourceBundlePrefix, locale, this.classLoader);
        } catch (MissingResourceException e) {
            return defaultResourceBundle();
        }
    }

    private ResourceBundle defaultResourceBundle() {
        return ResourceBundle.getBundle(this.resourceBundlePrefix, Locale.ENGLISH, this.classLoader);
    }
}