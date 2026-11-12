package com.example.dockingapp.host;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import com.example.dockingapp.plugin.Plugin;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.LinkedHashMap;

/** Shared application shell. App composition is determined by its Maven dependencies. */
public final class DockingApplication {
    private final List<Plugin> plugins;
    private final JFrame frame = new JFrame();
    private final CControl docking = new CControl(frame);
    private final JLabel localeLabel = new JLabel();
    private final Map<String, DefaultSingleCDockable> dockables = new LinkedHashMap<>();
    private Locale locale = Locale.ENGLISH;

    private DockingApplication(List<Plugin> plugins) {
        this.plugins = List.copyOf(plugins);
        configureFrame();
        for (Plugin plugin : this.plugins) {
            DefaultSingleCDockable dockable = new DefaultSingleCDockable(
                    plugin.getId(), plugin.getTitle(locale), plugin.createComponent(locale));
            docking.addDockable(dockable);
            dockable.setVisible(true);
            dockables.put(plugin.getId(), dockable);
        }
    }

    /** Finds plugin providers on this app's runtime classpath and opens the host. */
    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            List<Plugin> plugins = ServiceLoader.load(Plugin.class).stream()
                    .map(ServiceLoader.Provider::get)
                    .toList();
            new DockingApplication(plugins).frame.setVisible(true);
        });
    }

    private void configureFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(760, 480));
        frame.setSize(1000, 650);
        frame.setLocationRelativeTo(null);
        JPanel toolbar = new JPanel(new BorderLayout(8, 0));
        JComboBox<Language> languages = new JComboBox<>(new Language[] {
                new Language(Locale.ENGLISH, "English"),
                new Language(Locale.FRENCH, "Français"),
                new Language(Locale.GERMAN, "Deutsch")
        });
        languages.addActionListener(event -> {
            Language selected = (Language) languages.getSelectedItem();
            if (selected != null) changeLocale(selected.locale());
        });
        toolbar.add(localeLabel, BorderLayout.WEST);
        toolbar.add(languages, BorderLayout.EAST);
        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(docking.getContentArea(), BorderLayout.CENTER);
        updateHostText();
    }

    private void changeLocale(Locale newLocale) {
        locale = Objects.requireNonNull(newLocale);
        for (Plugin plugin : plugins) plugin.localeChanged(locale);
        updateHostText();
        for (Plugin plugin : plugins) {
            dockables.get(plugin.getId()).setTitleText(plugin.getTitle(locale));
        }
    }

    private void updateHostText() {
        var messages = java.util.ResourceBundle.getBundle("com.example.dockingapp.host.HostMessages", locale);
        frame.setTitle(messages.getString("app.title"));
        localeLabel.setText(messages.getString("locale.label"));
    }

    private record Language(Locale locale, String name) {
        @Override public String toString() { return name; }
    }
}
