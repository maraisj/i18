package com.example.dockingapp.host;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Owns the active locale and notifies registered UI components of changes. */
public final class LocaleManager {
    private final List<LocaleChangeListener> listeners = new ArrayList<>();
    private Locale locale;

    public LocaleManager(Locale initialLocale) {
        locale = Objects.requireNonNull(initialLocale, "initialLocale");
    }

    public Locale getLocale() {
        return locale;
    }

    public void addListener(LocaleChangeListener listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }

    public void setLocale(Locale newLocale) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> setLocale(newLocale));
            return;
        }
        if (locale.equals(newLocale)) {
            return;
        }
        locale = Objects.requireNonNull(newLocale, "newLocale");
        for (LocaleChangeListener listener : List.copyOf(listeners)) {
            listener.localeChanged(locale);
        }
    }

    @FunctionalInterface
    public interface LocaleChangeListener {
        void localeChanged(Locale locale);
    }
}
