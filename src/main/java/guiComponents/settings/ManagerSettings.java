package guiComponents.settings;

import guiComponents.JFrameEssentials;
import guiComponents.LimitDocumentFilter;
import guiComponents.RoundedBorder;
import guiComponents.guis.MainConsole;
import guiComponents.popups.TokenPopUp;
import other.WebhookGUI;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        settings.add(botStatus());

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

    /**
     * The settings panel for modifying the Bot's status
     * @return A {@link Supplier} with the instructions to create the {@link JPanel}/{@link Setting}
     */
    private static Supplier<JPanel> botStatus() {
        return () -> {
            // Create main panel
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
            mainPanel.setBackground(MID_GRAY);

            // Title Label
            JLabel label = new JLabel("Bot Status");
            label.setFont(new Font("Calibri",Font.BOLD,36));
            label.setForeground(WHITE);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setPreferredSize(new Dimension(200, 40));

            // JPanel for the "contents" of the setting
            JPanel settingsContents = new JPanel(new GridBagLayout());
            settingsContents.setBackground(null);

            // List of valid activities
            String[] activityTypes = new String[]{"Playing", "Watching", "Listening", "Competing"};

            // Get property
            String activity;
            try {
                activity = WebhookGUI.managerProperties.get("activity-type").toString();
            } catch (Exception e) {
                activity = WebhookGUI.getDefaultProperties().getProperty("activity-type");
            }
            String finalActivity = activity;

            // ComboBox for activity types
            JComboBox<String> activityType = new JComboBox<>(activityTypes);
            activityType.setBackground(LIGHTER_MID_GRAY);
            activityType.setForeground(WHITE);
            activityType.setBorder(BorderFactory.createLineBorder(DARK_GRAY, 2));
            activityType.setFont(new Font("Calibri",Font.PLAIN,20));
            activityType.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
            activityType.setPreferredSize(new Dimension(0, 60));
            activityType.setSelectedItem(IntStream.range(0, activityTypes.length)
                    .filter(i -> activityTypes[i].equals(finalActivity))
                    .mapToObj(i -> activityTypes[i])
                    .collect(Collectors.toList())
                    .get(0));
            JLabel comboLabel = ((JLabel)activityType.getRenderer());
            comboLabel.setHorizontalAlignment(JLabel.CENTER);

            // Get property
            String activityDescription;
            try {
                activityDescription = WebhookGUI.managerProperties.get("activity-desc").toString();
            } catch (Exception e) {
                activityDescription = WebhookGUI.getDefaultProperties().getProperty("activity-desc");
            }

            // Text Field for activity description
            JTextField activityDesc = new JTextField(activityDescription);
            activityDesc.setBackground(LIGHTER_MID_GRAY);
            activityDesc.setForeground(WHITE);
            activityDesc.setBorder(null);
            activityDesc.setFont(new Font("Calibri",Font.PLAIN,24));
            ((AbstractDocument)activityDesc.getDocument()).setDocumentFilter(new LimitDocumentFilter(128));

            // Scroll Pane for the Activity Description
            JScrollPane activityDescScroll = new JScrollPane(activityDesc);
            activityDescScroll.setBackground(LIGHTER_MID_GRAY);
            activityDescScroll.setBorder(BorderFactory.createLineBorder(DARK_GRAY, 2));
            activityDescScroll.setPreferredSize(new Dimension(0, 60));

            // "Save" Button
            JButton save = new JButton("Update");
            save.setBackground(GREEN);
            save.setBorder(new RoundedBorder(DARK_GRAY, 2, 16));
            save.setForeground(WHITE);
            save.setFont(new Font("Calibri",Font.BOLD,20));
            save.setFocusable(false);
            setHoverBrightnessChange(save, .25f);

            save.addActionListener(click -> {
                // Attempt to get each option
                Object activityTypeStr = activityType.getSelectedItem();
                String activityDescStr = activityDesc.getText();

                // Check if they are valid inputs
                if(activityTypeStr == null)
                    JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Must have an activity type selected!");
                else if(activityDescStr.length() == 0)
                    JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Description must be longer than 0 character!");

                // Update the properties
                WebhookGUI.managerProperties.put("activity-type", activityType.getSelectedItem());
                WebhookGUI.managerProperties.put("activity-desc", activityDescStr);
                WebhookGUI.writeProperties(WebhookGUI.managerProperties);

                // Update the activity
                WebhookGUI.GUI.BOT.getPresence().setActivity(
                        WebhookGUI.makeActivity(WebhookGUI.managerProperties.getProperty("activity-type"),
                                WebhookGUI.managerProperties.getProperty("activity-desc")
                        )
                );

                JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Activity updated!");
            });

            // GBC for formatting
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = .25;
            gbc.weighty = 1;
            gbc.insets = new Insets(0,0,0,5);

            // Add various elements
            settingsContents.add(activityType, gbc);
            gbc.weightx = .7;
            settingsContents.add(activityDescScroll, gbc);
            gbc.weightx = .05;
            gbc.insets = new Insets(0,0,0,0);
            settingsContents.add(save, gbc);

            // Add title/settingsContents
            mainPanel.add(label);
            mainPanel.add(settingsContents);

            return mainPanel;
        };
    }
}
