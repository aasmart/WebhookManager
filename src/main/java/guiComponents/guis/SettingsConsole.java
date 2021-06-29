package guiComponents.guis;

import guiComponents.JFrameEssentials;
import org.jetbrains.annotations.NotNull;
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

        // Main GUI Components
        add(frameTitle(), BorderLayout.NORTH);
        add(settingsPane(), BorderLayout.CENTER);

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

    /**
     * The title of the GUI
     * @return A {@link JPanel} with the title
     */
    @NotNull
    private JPanel frameTitle() {
        // Create the JPanel for housing the JLabel
        JPanel upper = new JPanel();
        upper.setLayout(new BorderLayout());
        upper.setBackground(NOT_QUITE_BLACK);

        // Create the title JLabel
        JLabel upperText = new JLabel("Settings",SwingConstants.CENTER);
        upperText.setFont(new Font("Calibri",Font.BOLD,36));
        upperText.setForeground(WHITE);

        // Add upperText to upper with formatting
        upper.add(padding(NOT_QUITE_BLACK), BorderLayout.NORTH);
        upper.add(upperText, BorderLayout.CENTER);

        return upper;
    }

    private JScrollPane settingsPane() {
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBorder(BorderFactory.createEmptyBorder());
        settingsPanel.setBackground(MID_GRAY);

        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBackground(MID_GRAY);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        return scrollPane;
    }
}
