package com.example.dockingapp.plugin.notes;

import com.example.dockingapp.host.LocaleManager;
import com.example.dockingapp.plugin.LocalizedPlugin;
import bibliothek.gui.dock.common.DefaultSingleCDockable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

/** A simple notes application contributed by the notes plugin. */
public final class NotesPlugin extends LocalizedPlugin {
    private final JLabel prompt = new JLabel();
    private final JTextArea notes = new JTextArea();
    private DefaultSingleCDockable dockable;

    public NotesPlugin(LocaleManager localeManager) {
        super(localeManager, "com.example.dockingapp.plugin.notes.Messages");
    }

    @Override
    public String getId() { return "notes"; }

    @Override
    public DefaultSingleCDockable createDockable() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.add(prompt, BorderLayout.NORTH);
        panel.add(new JScrollPane(notes), BorderLayout.CENTER);
        dockable = new DefaultSingleCDockable(getId(), text("title"), panel);
        updateTexts();
        return dockable;
    }

    @Override
    protected void updateTexts() {
        prompt.setText(text("prompt"));
        notes.setToolTipText(text("hint"));
        if (dockable != null) dockable.setTitleText(text("title"));
    }
}
