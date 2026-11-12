package com.example.dockingapp.plugin;

import com.example.dockingapp.host.LocaleManager;

import java.util.Locale;
import java.util.ResourceBundle;

/** Base class that keeps each plugin's translation bundle independent. */
public abstract class LocalizedPlugin implements Plugin {
    private final String bundleName;
    protected final LocaleManager localeManager;

    protected LocalizedPlugin(LocaleManager localeManager, String bundleName) {
        this.localeManager = localeManager;
        this.bundleName = bundleName;
        localeManager.addListener(this);
    }

    protected final String text(String key) {
        return ResourceBundle.getBundle(bundleName, localeManager.getLocale()).getString(key);
    }

    @Override
    public final void localeChanged(Locale locale) {
        updateTexts();
    }

    /** Refreshes all visible text owned by this plugin. */
    protected abstract void updateTexts();
}
