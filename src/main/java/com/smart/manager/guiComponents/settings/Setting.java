package com.smart.manager.guiComponents.settings;

import javax.swing.*;
import java.util.function.Supplier;

/**
 * A class representing a configurable setting in the Webhook Manager
 */
public class Setting {
    /**
     * The Setting's {@link JPanel}
     */
    private final JPanel settingsPanel;

    /**
     * Creates a Settings object from a {@link Supplier}
     * @param contents The {@link Supplier} containing the {@link JPanel}'s contents
     */
    public Setting(Supplier<JPanel> contents) {
        settingsPanel = contents.get();
    }

    /**
     * Get's the settings UI instance
     * @return A {@link JPanel}
     */
    public JPanel getSettingsPanel() {
        return settingsPanel;
    }
}
