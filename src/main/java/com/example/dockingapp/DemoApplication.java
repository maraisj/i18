package com.example.dockingapp;

import com.example.dockingapp.host.DockingApplication;
import com.example.dockingapp.host.LocaleManager;
import com.example.dockingapp.plugin.Plugin;
import com.example.dockingapp.plugin.counter.CounterPlugin;
import com.example.dockingapp.plugin.greeting.GreetingPlugin;
import com.example.dockingapp.plugin.notes.NotesPlugin;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.Locale;

/** Application entry point. Register additional plugins with the host here. */
public final class DemoApplication {
    private DemoApplication() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LocaleManager localeManager = new LocaleManager(Locale.ENGLISH);
            List<Plugin> plugins = List.of(
                    new GreetingPlugin(localeManager),
                    new CounterPlugin(localeManager),
                    new NotesPlugin(localeManager));
            new DockingApplication(localeManager, plugins).show();
        });
    }
}
