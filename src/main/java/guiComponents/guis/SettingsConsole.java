package guiComponents.guis;

import guiComponents.JFrameEssentials;
import guiComponents.settings.Setting;
import org.jetbrains.annotations.NotNull;
import other.WebhookGUI;

import javax.swing.*;
import java.awt.*;

/**
 * The "Console" for all of the {@link Setting}s
 */
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
        settingsPanel.setLayout(new BorderLayout());

        // Main GUI Components
        settingsPanel.add(settingsPane(), BorderLayout.CENTER);

        // Padding
        JPanel top = new JPanel();
        top.setBackground(NOT_QUITE_BLACK);
        top.setPreferredSize(new Dimension(0, 21));
        settingsPanel.add(top, BorderLayout.NORTH);

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
        settingsPanel.setBorder(BorderFactory.createMatteBorder(0,8,0,0, NOT_QUITE_BLACK));
        settingsPanel.setBackground(MID_GRAY);

        buildSettingsPanel(settingsPanel);

        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(MID_GRAY);

        return scrollPane;
    }

    /**
     * Takes a list of {@link guiComponents.settings.Setting}s and adds them to a given {@link JPanel}
     *
     * @param settingsContainer The {@link JPanel} to add the {@link Setting} objects to
     */
    private static void buildSettingsPanel(JPanel settingsContainer) {
        // Create GBC for formatting
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Remove all components from the JPanel
        settingsContainer.removeAll();

        // Add filler JPanel
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        settingsContainer.add(filler, gbc);

        // Update GBC constraints
        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = GridBagConstraints.RELATIVE;

        // Go through the list of webhooks and add them to the JPanel
        if (WebhookGUI.settings.size() != 0) {
            for (int i = WebhookGUI.settings.size() - 1; i >= 0; i--) {
                JPanel webhookPanel = WebhookGUI.settings.get(i).getSettingsPanel();
                settingsContainer.add(webhookPanel, gbc, 0);
            }
        }
    }

    /**
     * Turns the settings panel on/off depending on its state
     */
    public static void toggleSettings() {
        if(settingsPanel != null && isActive) {
            settingsPanel.setVisible(false);
            settingsTitle.setVisible(false);

            isActive = false;
        } else if(settingsPanel != null) {
            settingsPanel.setVisible(true);
            settingsTitle.setVisible(true);

            isActive = true;
        }

        WebhookGUI.GUI.MAIN_CONSOLE.revalidate();
        WebhookGUI.GUI.MAIN_CONSOLE.repaint();
    }
}
