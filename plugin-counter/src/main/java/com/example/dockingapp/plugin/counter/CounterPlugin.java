package com.example.dockingapp.plugin.counter;

import com.example.dockingapp.plugin.Plugin;
import com.example.dockingapp.plugin.TranslationService;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** A small counter application contributed by this plugin artifact. */
public final class CounterPlugin implements Plugin {
    private final JLabel countLabel = new JLabel();
    private final JButton incrementButton = new JButton();
    private int count;
    private TranslationService translations;

    @Override public String getId() { return "counter"; }
    @Override public void initialize(TranslationService translations) { this.translations = translations; }
    @Override public String getTitle() { return translations.text("title"); }

    @Override
    public JPanel createComponent() {
        JPanel panel = new JPanel();
        panel.add(countLabel);
        panel.add(incrementButton);
        incrementButton.addActionListener(event -> {
            count++;
            updateTexts();
        });
        updateTexts();
        return panel;
    }

    @Override
    public void localeChanged() {
        updateTexts();
    }

    private void updateTexts() {
        countLabel.setText(translations.text("count") + ": " + count);
        incrementButton.setText(translations.text("increment"));
    }
}
