package com.example.dockingapp.plugin.greeting;

import com.example.dockingapp.plugin.Plugin;
import com.example.dockingapp.plugin.TranslationService;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/** A standalone welcome application contributed by this plugin artifact. */
public final class GreetingPlugin implements Plugin {
    private JLabel message;

    @Override public String getId() { return "greeting"; }
    private TranslationService translations;

    @Override public void initialize(TranslationService translations) { this.translations = translations; }
    @Override public String getTitle() { return translations.text("title"); }

    @Override
    public JPanel createComponent() {
        message = new JLabel(translations.text("greeting", "Alex", "Sam"), JLabel.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(message, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void localeChanged() {
        if (message != null) message.setText(translations.text("greeting", "Alex", "Sam"));
    }
}
