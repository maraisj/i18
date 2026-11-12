package com.example.dockingapp.plugin;

import com.example.dockingapp.host.LocaleManager;

import bibliothek.gui.dock.common.DefaultSingleCDockable;

/** A small independently localized Swing application that the host can dock. */
public interface Plugin extends LocaleManager.LocaleChangeListener {
    /** Stable identifier used by the docking framework. */
    String getId();

    /** Creates the plugin's dockable window. */
    DefaultSingleCDockable createDockable();
}
