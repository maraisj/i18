package com.example.dockingapp.plugin;

import javax.swing.JComponent;
import java.util.Locale;

/** Contract implemented by independently packaged dockable applications. */
public interface Plugin {
    String getId();
    void initialize(TranslationService translations);
    String getTitle();
    JComponent createComponent();
    void localeChanged();
}
