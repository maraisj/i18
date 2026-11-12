package com.example.dockingapp.plugin.notes;

import com.example.dockingapp.plugin.Plugin;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.util.Locale;
import java.util.ResourceBundle;

/** A simple notes application contributed by this plugin artifact. */
public final class NotesPlugin implements Plugin {
    private final JLabel prompt = new JLabel();
    private final JTextArea notes = new JTextArea();

    @Override public String getId() { return "notes"; }
    @Override public String getTitle(Locale locale) { return text("title", locale); }

    @Override
    public JPanel createComponent(Locale locale) {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.add(prompt, BorderLayout.NORTH);
        panel.add(new JScrollPane(notes), BorderLayout.CENTER);
        updateTexts(locale);
        return panel;
    }

    @Override public void localeChanged(Locale locale) { updateTexts(locale); }

    private void updateTexts(Locale locale) {
        prompt.setText(text("prompt", locale));
        notes.setToolTipText(text("hint", locale));
    }

    private String text(String key, Locale locale) {
        return ResourceBundle.getBundle("com.example.dockingapp.plugin.notes.Messages", locale).getString(key);
    }
}
