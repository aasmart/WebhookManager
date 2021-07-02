package guiComponents.settings;

import guiComponents.JFrameEssentials;
import guiComponents.RoundedBorder;
import guiComponents.guis.MainConsole;
import guiComponents.popups.TokenPopUp;
import other.WebhookGUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * The Manager for all of the {@link Setting}s in the Webhook Manager
 */
public class ManagerSettings extends JFrameEssentials {
    /**
     * Takes all the settings methods and converts them into {@link Setting} objects
     * @return A {@link List} of {@link Setting}s objects
     */
    public static List<Setting> compileSettings() {
        List<Supplier<JPanel>> settings = new ArrayList<>();

        // Add the possible settings
        settings.add(botToken());

        return settings.stream().map(Setting::new).collect(Collectors.toList());
    }

    /**
     * Creates the Bot Token settings
     * @return A {@link Supplier} with the instructions to create the {@link JPanel}/{@link Setting}
     */
    private static Supplier<JPanel> botToken() {
        return () -> {
            // Main panel for all the components
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
            mainPanel.setBackground(MID_GRAY);

            // Title label
            JLabel label = new JLabel("Bot Token");
            label.setFont(new Font("Calibri",Font.BOLD,36));
            label.setForeground(WHITE);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setPreferredSize(new Dimension(200, 40));

            // The JTextField for the token
            JTextField token = new JTextField(WebhookGUI.readToken());
            token.setBackground(LIGHTER_MID_GRAY);
            token.setForeground(WHITE);
            token.setBorder(null);
            token.setFont(new Font("Calibri",Font.PLAIN,24));
            token.setEditable(false);

            // Scroll Pane for Token Field
            JScrollPane tokenScroll = new JScrollPane(token);
            tokenScroll.setBackground(LIGHTER_MID_GRAY);
            tokenScroll.setBorder(BorderFactory.createLineBorder(DARK_GRAY, 2));
            tokenScroll.setPreferredSize(new Dimension(0, 60));
            tokenScroll.setVisible(false);

            standardizeScrollbar(tokenScroll.getHorizontalScrollBar());
            standardizeScrollbar(tokenScroll.getVerticalScrollBar());

            // Reveal Button
            JButton reveal = new JButton("Reveal");
            reveal.setBackground(BLURPLE);
            reveal.setBorder(new RoundedBorder(DARK_GRAY, 2, 16));
            reveal.setForeground(WHITE);
            reveal.setFont(new Font("Calibri",Font.BOLD,20));
            reveal.setFocusable(false);
            setHoverBrightnessChange(reveal, .25f);

            reveal.addActionListener(click -> {
                tokenScroll.setVisible(!tokenScroll.isVisible());
                WebhookGUI.GUI.MAIN_CONSOLE.revalidate();
                WebhookGUI.GUI.MAIN_CONSOLE.repaint();
            });

            // Change Button
            JButton change = new JButton("Change");
            change.setBackground(BLURPLE);
            change.setBorder(new RoundedBorder(DARK_GRAY, 2, 16));
            change.setForeground(WHITE);
            change.setFont(new Font("Calibri",Font.BOLD,20));
            change.setFocusable(false);
            setHoverBrightnessChange(change, .25f);

            change.addActionListener(click -> {
                new TokenPopUp("Change Token!", MainConsole.class);
                change.setBackground(BLURPLE);
                WebhookGUI.GUI.MAIN_CONSOLE.dispose();
            });

            // Add title to mainPanel
            mainPanel.add(label);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

            // Create buttonPanel
            JPanel buttonPanel = new JPanel(new GridBagLayout());
            buttonPanel.setBackground(null);

            // Add all components to the buttonPanel
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = .75;
            gbc.weighty = 1;

            gbc.insets = new Insets(0,0,0,5);
            buttonPanel.add(tokenScroll, gbc);

            gbc.weightx = .125;
            gbc.insets = new Insets(0,0,0,5);
            buttonPanel.add(reveal, gbc);

            gbc.insets = new Insets(0,0,0,0);
            buttonPanel.add(change, gbc);

            // Add buttonPanel to mainPanel
            mainPanel.add(buttonPanel);

            return mainPanel;
        };
    }
}
