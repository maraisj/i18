package com.example.dockingapp.host;

import com.example.dockingapp.plugin.Plugin;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.DefaultSingleCDockable;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.Locale;

/** Main Swing host. Plugins appear as dockable application windows in its frame. */
public final class DockingApplication {
    private final LocaleManager localeManager;
    private final List<Plugin> plugins;
    private final JFrame frame = new JFrame();
    private final CControl docking = new CControl(frame);
    private final JLabel localeLabel = new JLabel();

    public DockingApplication(LocaleManager localeManager, List<Plugin> plugins) {
        this.localeManager = localeManager;
        this.plugins = List.copyOf(plugins);
        localeManager.addListener(locale -> updateHostText());
        configureFrame();
        configurePlugins();
        updateHostText();
    }

    public void show() {
        frame.setVisible(true);
    }

    private void configureFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(760, 480));
        frame.setSize(1000, 650);
        frame.setLocationRelativeTo(null);

        JPanel toolbar = new JPanel(new BorderLayout(8, 0));
        JComboBox<LanguageOption> languageSelector = new JComboBox<>(new LanguageOption[] {
                new LanguageOption(Locale.ENGLISH, "English"),
                new LanguageOption(Locale.FRENCH, "Français"),
                new LanguageOption(Locale.GERMAN, "Deutsch")
        });
        languageSelector.addActionListener(event -> {
            LanguageOption selected = (LanguageOption) languageSelector.getSelectedItem();
            if (selected != null) {
                localeManager.setLocale(selected.locale());
            }
        });
        toolbar.add(localeLabel, BorderLayout.WEST);
        toolbar.add(languageSelector, BorderLayout.EAST);
        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(docking.getContentArea(), BorderLayout.CENTER);
    }

    private void configurePlugins() {
        for (Plugin plugin : plugins) {
            DefaultSingleCDockable dockable = plugin.createDockable();
            docking.addDockable(dockable);
            dockable.setVisible(true);
        }
    }

    private void updateHostText() {
        localeLabel.setText(hostText("locale.label", localeManager.getLocale()));
        frame.setTitle(hostText("app.title", localeManager.getLocale()));
    }

    private String hostText(String key, Locale locale) {
        return java.util.ResourceBundle.getBundle("com.example.dockingapp.host.HostMessages", locale).getString(key);
    }

    private record LanguageOption(Locale locale, String label) {
        @Override
        public String toString() {
            return label;
        }
    }
}
