package com.example.dockingapp.plugin;

import javax.swing.JComponent;
import java.util.Locale;

/** Contract implemented by independently packaged dockable applications. */
public interface Plugin {
    String getId();
    String getTitle(Locale locale);
    JComponent createComponent(Locale locale);
    void localeChanged(Locale locale);
}
