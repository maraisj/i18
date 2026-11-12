package com.example.dockingapp.plugin.greeting;

import com.example.dockingapp.host.LocaleManager;
import com.example.dockingapp.plugin.LocalizedPlugin;
import bibliothek.gui.dock.common.DefaultSingleCDockable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/** A tiny welcome application contributed by the greeting plugin. */
public final class GreetingPlugin extends LocalizedPlugin {
    private final JLabel message = new JLabel();
    private DefaultSingleCDockable dockable;

    public GreetingPlugin(LocaleManager localeManager) {
        super(localeManager, "com.example.dockingapp.plugin.greeting.Messages");
    }

    @Override
    public String getId() { return "greeting"; }

    @Override
    public DefaultSingleCDockable createDockable() {
        JPanel panel = new JPanel(new BorderLayout());
        message.setHorizontalAlignment(JLabel.CENTER);
        panel.add(message, BorderLayout.CENTER);
        dockable = new DefaultSingleCDockable(getId(), text("title"), panel);
        updateTexts();
        return dockable;
    }

    @Override
    protected void updateTexts() {
        message.setText(text("message"));
        if (dockable != null) dockable.setTitleText(text("title"));
    }
}
