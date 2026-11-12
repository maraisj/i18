package com.example.dockingapp.plugin;

/** Message lookup scoped to one plugin's resource bundle. */
public interface TranslationService {
    /** Returns a translated message and formats any MessageFormat arguments. */
    String text(String key, Object... arguments);
}
