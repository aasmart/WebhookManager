package guiComponents.guis;

import guiComponents.JFrameEssentials;
import other.WebhookGUI;

import javax.swing.*;
import java.awt.*;

public class SettingsConsole extends JFrameEssentials {
    public SettingsConsole() {
        // Setup basic console
        setTitle("Webhook Manager Settings");
        setSize(600, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setBackground(NOT_QUITE_BLACK);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Padding
        add(padding(NOT_QUITE_BLACK), BorderLayout.WEST);
        add(padding(NOT_QUITE_BLACK), BorderLayout.EAST);

        // Add various panels

        // Create listener for window closing event
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            WebhookGUI.GUI.MAIN_CONSOLE.populateList();
            WebhookGUI.GUI.MAIN_CONSOLE.setEnabled(true);
            }
        });

        setVisible(true);
    }
}
