package com.smart.manager.guiComponents.guis;

import com.smart.manager.guiComponents.JFrameEssentials;
import com.smart.manager.guiComponents.LimitDocumentFilter;
import com.smart.manager.guiComponents.RoundedBorder;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import com.smart.manager.Webhook;
import com.smart.manager.WebhookGUI;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * The {@link JFrame} for creating {@link Webhook}s
 */
public class WebhookCreateConsole extends JFrameEssentials {
    /**
     * The {@link JTextField} for the name of the {@link Webhook}
     */
    private JTextField webhookNameBox;

    /**
     * The {@link File} for the avatar
     */
    private File avatarFile;

    /**
     * The {@link JComboBox} for all the channel in the {@link net.dv8tion.jda.api.entities.Guild}
     */
    private JComboBox<String> channelIDField;

    /**
     * The ID of the {@link net.dv8tion.jda.api.entities.Guild} the {@link Webhook} is being created in
     */
    private final long guildID;

    /**
     * Creates a {@link WebhookCreateConsole} which is for creating {@link Webhook Webhooks}
     * @param guildID The ID of the {@link Guild} the {@link Webhook} is being created in
     */
    public WebhookCreateConsole(long guildID) {
        this.guildID = guildID;

        updateMainConsole();
    }

    /**
     * Updates the GUI's Main display panel
     */
    public void updateMainConsole() {
        MainConsole mainConsole = WebhookGUI.GUI.MAIN_CONSOLE;
        mainConsole.getContentPane().removeAll();

        // Basic settings for JFrame
        mainConsole.getContentPane().setLayout(new BorderLayout());
        mainConsole.getContentPane().setBackground(NOT_QUITE_BLACK);

        // Add JPanels to JFrame
        mainConsole.getContentPane().add(frameTitle(), BorderLayout.NORTH);
        mainConsole.getContentPane().add(createPanel(), BorderLayout.CENTER);

        // Padding
        mainConsole.getContentPane().add(padding(NOT_QUITE_BLACK), BorderLayout.WEST);
        mainConsole.getContentPane().add(padding(NOT_QUITE_BLACK), BorderLayout.EAST);

        // Create GBC for formatting
        GridBagConstraints gbc = new GridBagConstraints();

        // Create the bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(NOT_QUITE_BLACK);
        bottomPanel.setLayout(new GridBagLayout());

        // Update constraints and add the createWebhook() field
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = .9;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 0);
        bottomPanel.add(createWebhook(), gbc);

        // Update constraints and add the bottom panel
        gbc.weightx = .1;
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        bottomPanel.add(cancel(), gbc);
        mainConsole.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        mainConsole.validate();
        mainConsole.repaint();
    }

    /**
     * Creates the {@link JPanel} housing the title for the {@link WebhookCreateConsole}
     *
     * @return A {@link JPanel}
     */
    @NotNull
    private JPanel frameTitle() {
        // Create the JPanel housing the title label
        JPanel upper = new JPanel();
        upper.setLayout(new BorderLayout());
        upper.setBackground(NOT_QUITE_BLACK);

        // Create the title label, and formatting
        JLabel upperText = new JLabel("Webhook Creator",SwingConstants.CENTER);
        upperText.setFont(new Font("Calibri",Font.BOLD,36));
        upperText.setForeground(WHITE);

        // Add padding and title to the main JPanel
        upper.add(padding(NOT_QUITE_BLACK), BorderLayout.NORTH);
        upper.add(upperText, BorderLayout.CENTER);

        return upper;
    }

    /**
     * Creates the {@link JPanel} that houses all the fields for the user input to create a {@link Webhook}
     *
     * @return A {@link JPanel}
     */
    @NotNull
    private JPanel createPanel() {
        // Create main JPanel
        JPanel createPanel = new JPanel();
        createPanel.setBackground(MID_GRAY);
        createPanel.setLayout(new GridBagLayout());

        // Create the JPanel that goes on the left
        JPanel topInputPanel = new JPanel();
        topInputPanel.setOpaque(false);
        topInputPanel.setLayout(new BoxLayout(topInputPanel, BoxLayout.LINE_AXIS));

        // Create the JPanel for the webhook name field and add it to the left panel
        JPanel webhookName = webhookName();
        topInputPanel.add(webhookName);

        topInputPanel.add(Box.createRigidArea(new Dimension(30, 0)));

        // Create JPanel for the avatar field and add it to the right panel
        JPanel avatarField = avatarField();
        topInputPanel.add(avatarField);

        // Create the JPanel that goes on the right
        JPanel rightCreatePanel = new JPanel();
        rightCreatePanel.setOpaque(false);
        rightCreatePanel.setLayout(new BoxLayout(rightCreatePanel, BoxLayout.PAGE_AXIS));

        // Create the JPanel for the channel ID field and add it to the right panel
        JPanel channelIDField = channelIDField();
        rightCreatePanel.add(channelIDField);

        // Initial GBC for formatting
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(0,30,0,30);
        createPanel.add(topInputPanel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(30,30,0,30);
        createPanel.add(rightCreatePanel, gbc);

        return createPanel;
    }

    /**
     * Creates the field for entering the name of the {@link Webhook}
     *
     * @return A {@link JPanel}
     */
    @NotNull
    private JPanel webhookName() {
        // The main JPanel
        JPanel nameField = new JPanel();
        nameField.setOpaque(false);
        nameField.setLayout(new BoxLayout(nameField, BoxLayout.PAGE_AXIS));

        // Create the JLabel for the title and formatting
        JLabel label = new JLabel("Webhook Name", SwingConstants.CENTER);
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);

        // JPanel for title
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setOpaque(false);
        labelPanel.add(label, BorderLayout.CENTER);
        labelPanel.setPreferredSize(new Dimension(200, 40));

        // Add title to main panel
        nameField.add(labelPanel);

        // Create the field for entering the webhook name and formatting
        webhookNameBox = new JTextField();
        ((AbstractDocument)webhookNameBox.getDocument()).setDocumentFilter(new LimitDocumentFilter(80));
        webhookNameBox.setBackground(LIGHTER_MID_GRAY);
        webhookNameBox.setForeground(WHITE);
        webhookNameBox.setBorder(BorderFactory.createLineBorder(DARK_GRAY, 2));
        webhookNameBox.setFont(new Font("Calibri",Font.PLAIN,20));
        webhookNameBox.setText("Web Hooker");
        webhookNameBox.setHorizontalAlignment(JTextField.CENTER);

        // Create JScrollPane for the webhook name field, and formatting
        JScrollPane textScroller = new JScrollPane(webhookNameBox);
        textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScroller.setBackground(LIGHTER_MID_GRAY);
        textScroller.setBorder(BorderFactory.createEmptyBorder());
        textScroller.setPreferredSize(new Dimension(0, 60));

        standardizeScrollbar(textScroller.getHorizontalScrollBar());
        standardizeScrollbar(textScroller.getVerticalScrollBar());

        // Add scroll pane to the main panel
        nameField.add(textScroller);

        return nameField;
    }

    /**
     * Creates the field for selecting the avatar file for the {@link Webhook}
     *
     * @return A {@link JPanel}
     */
    @NotNull
    private JPanel avatarField() {
        // The main JPanel
        JPanel avatarPanel = new JPanel();
        avatarPanel.setOpaque(false);
        avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.PAGE_AXIS));

        // Create the JLabel to be the title, and formatting
        JLabel label = new JLabel("Avatar Image", SwingConstants.CENTER);
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);

        // JPanel for title
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setOpaque(false);
        labelPanel.add(label, BorderLayout.CENTER);
        labelPanel.setPreferredSize(new Dimension(200, 40));

        // Add title to main panel
        avatarPanel.add(labelPanel);

        // Add the sub JPanel for the file fields
        JPanel subPanel = new JPanel();
        subPanel.setBackground(MID_GRAY);
        subPanel.setLayout(new GridBagLayout());

        // Creates the JTextField which will contain the name of the file, and formatting
        JTextField avatarFileName = new JTextField();
        avatarFileName.setBackground(LIGHTER_MID_GRAY);
        avatarFileName.setForeground(WHITE);
        avatarFileName.setBorder(BorderFactory.createLineBorder(DARK_GRAY, 2));
        avatarFileName.setFont(new Font("Calibri",Font.PLAIN,20));
        avatarFileName.setText("No file selected...");
        avatarFileName.setEditable(false);

        // Create the JScrollPane for the avatar file name field
        JScrollPane avatarFileScroll = new JScrollPane(avatarFileName);
        avatarFileScroll.setBorder(BorderFactory.createEmptyBorder());
        avatarFileScroll.setPreferredSize(new Dimension(0, 60));
        avatarFileScroll.setBackground(LIGHTER_MID_GRAY);

        standardizeScrollbar(avatarFileScroll.getHorizontalScrollBar());
        standardizeScrollbar(avatarFileScroll.getVerticalScrollBar());

        // Create the JButton for opening the file browser, and formatting
        JButton selectAvatarButton = new JButton("Find..");
        selectAvatarButton.setToolTipText("Select avatar");
        selectAvatarButton.setBackground(BLURPLE);
        selectAvatarButton.setForeground(WHITE);
        selectAvatarButton.setFont(new Font("Calibri",Font.BOLD,16));
        selectAvatarButton.setBorder(new RoundedBorder(DARK_GRAY, 2, 4));
        setHoverBrightnessChange(selectAvatarButton, .25f);
        selectAvatarButton.setFocusable(false);

        // File chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setPreferredSize(new Dimension(800, 600));
        fileChooser.setDialogTitle("Select Avatar File");
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase(Locale.ROOT).endsWith(".png");
            }

            @Override
            public String getDescription() {
                return ".png";
            }
        });
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Add the action listener for opening the JFileChooser once the "Choose File" button is pressed
        selectAvatarButton.addActionListener(event -> {
            fileChooser.getActionMap().get("viewTypeDetails").actionPerformed(null);
            if (fileChooser.showDialog(this, "Load") == JFileChooser.APPROVE_OPTION) {
                avatarFile = fileChooser.getSelectedFile();
                avatarFileName.setText(avatarFile.getAbsolutePath());
            }
        });

        // Create GBC for formatting and add the avatar JScrollPane
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .8;
        gbc.weighty = 1;
        gbc.gridy = 0;
        subPanel.add(avatarFileScroll, gbc);

        // JPanel for button
        JPanel avatarButtonPanel = new JPanel(new BorderLayout());
        avatarButtonPanel.setOpaque(false);
        avatarButtonPanel.add(selectAvatarButton, BorderLayout.CENTER);

        // Update GBC and add the button
        gbc.weightx = .2;
        gbc.insets = new Insets(0, 10, 0, 0);
        subPanel.add(avatarButtonPanel, gbc);

        // Add the sub panel to the main panel
        avatarPanel.add(subPanel);

        return avatarPanel;
    }

    /**
     * Creates the field for selecting the channel where the {@link Webhook} will be created in
     *
     * @return A {@link JPanel}
     */
    @NotNull
    @SuppressWarnings("DuplicatedCode")
    private JPanel channelIDField() {
        // Create main JPanel
        JPanel field = new JPanel();
        field.setOpaque(false);
        field.setLayout(new BoxLayout(field, BoxLayout.PAGE_AXIS));

        // Create JLabel for the tile, and formatting
        JLabel label = new JLabel("Channel");
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setPreferredSize(new Dimension(200, 40));
        field.add(label);

        // Fetch the list of channels in the server
        Guild g = WebhookGUI.GUI.BOT.getGuildById(guildID);
        if(g == null) {
            JOptionPane.showMessageDialog(this, "This Guild no longer exists");
            throw new NullPointerException("Guild with ID " + guildID + " is null!");
        }
        List<String> tempList = g.getTextChannels().stream().map(channel -> channel.getName() + ":" + channel.getId()).collect(Collectors.toList());
        String[] channels = new String[tempList.size()];

        // Create the JComboBox for the channels, and formatting
        channelIDField = new JComboBox<>(tempList.toArray(channels));
        channelIDField.setBackground(LIGHTER_MID_GRAY);
        channelIDField.setForeground(WHITE);
        channelIDField.setBorder(BorderFactory.createLineBorder(DARK_GRAY, 2));
        channelIDField.setFont(new Font("Calibri",Font.PLAIN,20));
        channelIDField.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        JLabel comboLabel = ((JLabel)channelIDField.getRenderer());
        comboLabel.setHorizontalAlignment(JLabel.CENTER);

        // TODO Fix scroll wheel setting its size based on options currently not selected
        // Create the JScrollPane for the combo box
        JScrollPane textScroller = new JScrollPane(channelIDField);
        textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScroller.setBackground(LIGHTER_MID_GRAY);
        textScroller.setBorder(BorderFactory.createEmptyBorder());
        textScroller.setPreferredSize(new Dimension(0, 60));

        // Add text scroller to main panel
        field.add(textScroller);

        return field;
    }

    /**
     * Creates the {@link JPanel} for housing the {@link JButton} for creating the {@link Webhook} and the {@link JButton} for canceling
     *
     * @return A {@link JPanel}
     */
    @NotNull
    private JPanel createWebhook() {
        // Create main JPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(DARK_GRAY);
        buttonPanel.setLayout(new GridBagLayout());

        // Create the "Create Webhook" button, and formatting
        JButton createWebhook = new JButton("Finalize Webhook");
        createWebhook.setBackground(BLURPLE);
        createWebhook.setForeground(WHITE);
        createWebhook.setFont(new Font("Calibri", Font.BOLD, 40));
        createWebhook.setBorder(new RoundedBorder(MID_GRAY, 0, 16));
        createWebhook.setFocusable(false);
        setHoverBrightnessChange(createWebhook, .25f);

        // Add the action listener to the create button for creating the Webhook on click
        createWebhook.addActionListener(event -> {
            // Get username and Channel ID
            String username = webhookNameBox.getText();
            String channelID = String.valueOf(channelIDField.getSelectedItem()).split(":")[1];

            // Do checks for fields
            if (username.length() == 0)
                JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Username must be longer than 0 characters.");
            else if (channelID.length() == 0)
                JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Must specify channel.");
            else {
                if (Webhook.createWebhook(username, avatarFile, channelID)) {
                    WebhookGUI.GUI.MAIN_CONSOLE.updateMainConsole();
                    if(Boolean.parseBoolean(WebhookGUI.managerProperties.getProperty("success-popups")))
                        JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Webhook created!");
                }
            }
        });

        // Create GBC for formatting
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        // Add the buttons to the button panel
        buttonPanel.add(createWebhook, gbc);

        return buttonPanel;
    }
}
