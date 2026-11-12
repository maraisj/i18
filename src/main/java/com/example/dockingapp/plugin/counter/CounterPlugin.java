package com.example.dockingapp.plugin.counter;

import com.example.dockingapp.host.LocaleManager;
import com.example.dockingapp.plugin.LocalizedPlugin;
import bibliothek.gui.dock.common.DefaultSingleCDockable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/** A small counter application contributed by the counter plugin. */
public final class CounterPlugin extends LocalizedPlugin {
    private final JLabel countLabel = new JLabel();
    private final JButton incrementButton = new JButton();
    private int count;
    private DefaultSingleCDockable dockable;

    public CounterPlugin(LocaleManager localeManager) {
        super(localeManager, "com.example.dockingapp.plugin.counter.Messages");
        incrementButton.addActionListener(event -> {
            count++;
            updateTexts();
        });
    }

    @Override
    public String getId() { return "counter"; }

    @Override
    public DefaultSingleCDockable createDockable() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(countLabel);
        panel.add(incrementButton);
        dockable = new DefaultSingleCDockable(getId(), text("title"), panel);
        updateTexts();
        return dockable;
    }

    @Override
    protected void updateTexts() {
        countLabel.setText(text("count") + ": " + count);
        incrementButton.setText(text("increment"));
        if (dockable != null) dockable.setTitleText(text("title"));
    }
}
