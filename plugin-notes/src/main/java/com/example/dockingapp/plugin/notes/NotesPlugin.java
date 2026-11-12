package com.example.dockingapp.plugin.notes;

import com.example.dockingapp.plugin.Plugin;
import com.example.dockingapp.plugin.TranslationService;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

/** A simple notes application contributed by this plugin artifact. */
public final class NotesPlugin implements Plugin {
    private final JLabel prompt = new JLabel();
    private final JTextArea notes = new JTextArea();
    private TranslationService translations;

    @Override public String getId() { return "notes"; }
    @Override public void initialize(TranslationService translations) { this.translations = translations; }
    @Override public String getTitle() { return translations.text("title"); }

    @Override
    public JPanel createComponent() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.add(prompt, BorderLayout.NORTH);
        panel.add(new JScrollPane(notes), BorderLayout.CENTER);
        updateTexts();
        return panel;
    }

    @Override public void localeChanged() { updateTexts(); }

    private void updateTexts() {
        prompt.setText(translations.text("prompt"));
        notes.setToolTipText(translations.text("hint"));
    }
}
