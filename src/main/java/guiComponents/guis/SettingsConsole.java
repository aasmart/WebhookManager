package guiComponents.guis;

import guiComponents.JFrameEssentials;
import org.jetbrains.annotations.NotNull;
import other.WebhookGUI;

import javax.swing.*;
import java.awt.*;

public class SettingsConsole extends JFrameEssentials {
    /**
     * If the settings console is currently open (True if so)
     */
    private static boolean isActive;

    /**
     * The current Settings Panel
     */
    private static JPanel settingsPanel;

    /**
     * The current Settings Title
     */
    private static JPanel settingsTitle;

    /**
     * Creates the Settings Console {@link JPanel} which houses all the settings for the application
     * @param panel A {@link JPanel}
     */
    public static void buildSettingsConsole(JPanel panel) {
        isActive = false;

        // Setup basic console
        settingsPanel = panel;
        settingsPanel.setBackground(NOT_QUITE_BLACK);

        // Main GUI Components
        settingsPanel.add(settingsPane(), BorderLayout.CENTER);

        // Padding
        settingsPanel.add(padding(NOT_QUITE_BLACK), BorderLayout.WEST);
        settingsPanel.add(padding(NOT_QUITE_BLACK), BorderLayout.EAST);

        settingsPanel.setVisible(false);
    }

    /**
     * The title of the GUI
     * @return A {@link JPanel} with the title
     */
    @NotNull
    public static JPanel frameTitle() {
        // Create the JPanel for housing the JLabel
        JPanel upper = new JPanel();
        upper.setLayout(new BorderLayout());
        upper.setBackground(NOT_QUITE_BLACK);

        // Create the title JLabel
        JLabel upperText = new JLabel("Settings",SwingConstants.CENTER);
        upperText.setFont(new Font("Calibri",Font.BOLD,36));
        upperText.setForeground(WHITE);

        // Add upperText to upper with formatting
        upper.add(upperText, BorderLayout.CENTER);

        settingsTitle = upper;
        upper.setVisible(false);

        return upper;
    }

    /**
     * Creates the panel housing the list of all settings
     * @return A {@link JScrollPane}
     */
    private static JScrollPane settingsPane() {
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBorder(BorderFactory.createEmptyBorder());
        settingsPanel.setBackground(MID_GRAY);

        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBackground(MID_GRAY);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        return scrollPane;
    }

    /**
     * Turns the settings panel on/off depending on its state
     */
    public static void toggleSettings() {
        if(settingsPanel != null && isActive) {
            settingsPanel.setVisible(false);
            settingsTitle.setVisible(false);
            WebhookGUI.GUI.MAIN_CONSOLE.revalidate();
            WebhookGUI.GUI.MAIN_CONSOLE.repaint();
            isActive = false;
        } else if(settingsPanel != null) {
            settingsPanel.setVisible(true);
            settingsTitle.setVisible(true);
            WebhookGUI.GUI.MAIN_CONSOLE.revalidate();
            WebhookGUI.GUI.MAIN_CONSOLE.repaint();
            isActive = true;
        }
    }
}
