package com.example.dockingapp.host;

import com.example.dockingapp.plugin.TranslationService;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/** ResourceBundle-backed translator bound to one plugin and the host's active locale. */
final class ResourceBundleTranslationService implements TranslationService {
    private final String bundleName;
    private Locale locale;

    ResourceBundleTranslationService(String pluginId, Locale locale) {
        this.bundleName = "com.example.dockingapp.plugin." + pluginId + ".Messages";
        this.locale = Objects.requireNonNull(locale, "locale");
    }

    void setLocale(Locale locale) {
        this.locale = Objects.requireNonNull(locale, "locale");
    }

    @Override
    public String text(String key, Object... arguments) {
        String pattern = ResourceBundle.getBundle(bundleName, locale).getString(key);
        return new MessageFormat(pattern, locale).format(arguments);
    }
}
