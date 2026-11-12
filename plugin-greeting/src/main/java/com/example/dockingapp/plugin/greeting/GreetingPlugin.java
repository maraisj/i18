package com.example.dockingapp.plugin.greeting;

import com.example.dockingapp.plugin.Plugin;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.Locale;
import java.util.ResourceBundle;

/** A standalone welcome application contributed by this plugin artifact. */
public final class GreetingPlugin implements Plugin {
    private JLabel message;

    @Override public String getId() { return "greeting"; }
    @Override public String getTitle(Locale locale) { return text("title", locale); }

    @Override
    public JPanel createComponent(Locale locale) {
        message = new JLabel(text("message", locale), JLabel.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(message, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void localeChanged(Locale locale) {
        if (message != null) message.setText(text("message", locale));
    }

    private String text(String key, Locale locale) {
        return ResourceBundle.getBundle("com.example.dockingapp.plugin.greeting.Messages", locale).getString(key);
    }
}
