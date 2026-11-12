package com.example.dockingapp.plugin.counter;

import com.example.dockingapp.plugin.Plugin;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Locale;
import java.util.ResourceBundle;

/** A small counter application contributed by this plugin artifact. */
public final class CounterPlugin implements Plugin {
    private final JLabel countLabel = new JLabel();
    private final JButton incrementButton = new JButton();
    private int count;

    @Override public String getId() { return "counter"; }
    @Override public String getTitle(Locale locale) { return text("title", locale); }

    @Override
    public JPanel createComponent(Locale locale) {
        localeForCurrentUi = locale;
        JPanel panel = new JPanel();
        panel.add(countLabel);
        panel.add(incrementButton);
        incrementButton.addActionListener(event -> {
            count++;
            updateTexts(localeForCurrentUi);
        });
        updateTexts(locale);
        return panel;
    }

    private Locale localeForCurrentUi = Locale.ENGLISH;

    @Override
    public void localeChanged(Locale locale) {
        localeForCurrentUi = locale;
        updateTexts(locale);
    }

    private void updateTexts(Locale locale) {
        countLabel.setText(text("count", locale) + ": " + count);
        incrementButton.setText(text("increment", locale));
    }

    private String text(String key, Locale locale) {
        return ResourceBundle.getBundle("com.example.dockingapp.plugin.counter.Messages", locale).getString(key);
    }
}
