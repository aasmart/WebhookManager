package com.smart.manager.guiComponents.guis;

import com.smart.manager.Webhook;
import com.smart.manager.guiComponents.JFrameEssentials;
import com.smart.manager.guiComponents.LimitDocumentFilter;
import com.smart.manager.guiComponents.RoundedBorder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import com.smart.manager.DiscordAPI;
import com.smart.manager.WebhookGUI;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The console for managing {@link Webhook}s
 */
public class WebhookConsole extends JFrameEssentials {
    /**
     * The {@link JTextField} for housing the {@link Webhook}'s username
     */
    private JTextField usernameBox;

    /**
     * The {@link JTextField} for housing the {@link Webhook}'s avatar URL
     */
    private JTextField avatarURL;

    private JComboBox<String> channelIDField;

    /**
     * The ID of the {@link Webhook} the user is currently managing
     */
    private final String id;

    /**
     * The token of the {@link Webhook} the user is currently managing
     */
    private final String token;

    /**
     * The basic constructor for the {@link WebhookConsole} {@link JFrame}.
     *
     * @param id The ID of the {@link Webhook} the user is managing
     * @param token The token of the {@link Webhook} the user is managing
     */
    public WebhookConsole(String id, String token) {
        this.id = id;
        this.token = token;

        // Basic formatting for the JFrame
        setTitle("Webhook Message Console");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setBackground(NOT_QUITE_BLACK);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Add important components
        add(frameTitle(), BorderLayout.NORTH);
        JPanel userInputPanel = userInputPanel();
        if(userInputPanel == null)
            return;
        add(userInputPanel, BorderLayout.CENTER);

        // Create padding
        add(padding(NOT_QUITE_BLACK), BorderLayout.WEST);
        add(padding(NOT_QUITE_BLACK), BorderLayout.EAST);

        // Create listener for window closing event
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                WebhookGUI.GUI.MAIN_CONSOLE.populateList(WebhookGUI.GUI.MAIN_CONSOLE.tabGuildIDMap.get(WebhookGUI.GUI.MAIN_CONSOLE.guildPanel.getSelectedIndex()));
                WebhookGUI.GUI.MAIN_CONSOLE.setEnabled(true);
            }
        });

        setVisible(true);
    }

    /**
     * Creates a {@link JPanel} for housing the title of the {@link JFrame}
     *
     * @return A {@link JPanel}
     */
    @NotNull
    private JPanel frameTitle() {
        // Create JPanel for housing the label
        JPanel upper = new JPanel();
        upper.setLayout(new BorderLayout());
        upper.setBackground(NOT_QUITE_BLACK);

        // Create JLabel and formatting
        JLabel upperText = new JLabel("Webhook Message Sender",SwingConstants.CENTER);
        upperText.setFont(new Font("Calibri",Font.BOLD,36));
        upperText.setForeground(WHITE);

        // Add components to the JPanel
        upper.add(padding(NOT_QUITE_BLACK), BorderLayout.NORTH);
        upper.add(upperText, BorderLayout.CENTER);

        return upper;
    }

    /**
     * Creates the {@link JPanel} for all the fields the user can enter values to for sending the message. This houses the components created by
     * {@link WebhookConsole#usernameField()}, {@link WebhookConsole#avatarURLField()}, and {@link WebhookConsole#message()}.
     *
     * @return A {@link JPanel}
     */
    private JPanel userInputPanel() {
        // Create the main JPanel and formatting
        JPanel panel = new JPanel();
        panel.setBackground(MID_GRAY);
        panel.setLayout(new GridBagLayout());

        // Create the JPanel that goes on the top row of the main JPanel
        JPanel subPanelTop = new JPanel();
        subPanelTop.setOpaque(false);
        subPanelTop.setLayout(new GridBagLayout());

        // Create GBC for formatting
        GridBagConstraints gbcSub = new GridBagConstraints();
        gbcSub.gridx = 0;
        gbcSub.gridy = 0;
        gbcSub.weightx = .4;
        gbcSub.weighty = 1;
        gbcSub.fill = GridBagConstraints.BOTH;
        gbcSub.insets = new Insets(40, 40, 40, 20);

        // Add the username field to the top sub JPanel
        subPanelTop.add(usernameField(), gbcSub);

        // Update constraints and add the avatar field to the top sub JPanel
        gbcSub.gridx = 1;
        gbcSub.weightx = .3;
        gbcSub.insets = new Insets(40, 0, 40, 20);
        subPanelTop.add(avatarURLField(), gbcSub);

        // Update constraints and add the channel ID field to the top sub JPanel
        gbcSub.gridx = 2;
        gbcSub.insets = new Insets(40, 0, 40, 40);
        JPanel channelField = channelIDField();
        if(channelField == null)
            return null;
        subPanelTop.add(channelField , gbcSub);

        // Create new GBC for the top sub panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = .1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(subPanelTop, gbc);

        // Update constraints and add the message field to the main JPanel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = .9;
        gbc.weightx = 1;
        gbc.insets = new Insets(0,40,40,40);
        panel.add(message(), gbc);

        return panel;
    }

    /**
     * Creates the field for entering the {@link Webhook}'s username when sending a message
     *
     * @return A {@link JPanel}
     */
    @NotNull
    @SuppressWarnings("DuplicatedCode")
    private JPanel usernameField() {
        // Create JPanel for housing the JLabel/JTextField
        JPanel field = new JPanel();
        field.setOpaque(false);
        field.setLayout(new BoxLayout(field, BoxLayout.PAGE_AXIS));

        // Create the JLabel (Title)
        JLabel label = new JLabel("Username");
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setPreferredSize(new Dimension(0, 50));
        field.add(label);
        field.add(Box.createRigidArea(new Dimension(0,5)));

        // Create the JTextField for entering the Webhook's nickname
        usernameBox = new JTextField();
        ((AbstractDocument)usernameBox.getDocument()).setDocumentFilter(new LimitDocumentFilter(80));
        usernameBox.setBackground(LIGHTER_MID_GRAY);
        usernameBox.setForeground(WHITE);
        usernameBox.setBorder(BorderFactory.createLineBorder(DARK_GRAY, 2));
        usernameBox.setFont(new Font("Calibri",Font.PLAIN,20));
        usernameBox.setHorizontalAlignment(JTextField.CENTER);

        // Add the username text field to a JScrollPane
        JScrollPane textScroller = new JScrollPane(usernameBox);
        textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScroller.setBackground(LIGHTER_MID_GRAY);
        textScroller.setBorder(BorderFactory.createEmptyBorder());
        textScroller.setPreferredSize(new Dimension(0,35));

        // Add the text scroller to the main JPanel
        field.add(textScroller);

        return field;
    }

    /**
     * Creates the field for entering the {@link Webhook}'s avatar for when sending a message
     *
     * @return A {@link JPanel}
     */
    @NotNull
    @SuppressWarnings("DuplicatedCode")
    private JPanel avatarURLField() {
        // Create main JPanel & formatting
        JPanel mainField = new JPanel();
        mainField.setOpaque(false);
        mainField.setLayout(new BoxLayout(mainField, BoxLayout.PAGE_AXIS));

        // Create JLabel for title
        JLabel label = new JLabel("Avatar URL");
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setPreferredSize(new Dimension(0, 50));
        mainField.add(label);
        mainField.add(Box.createRigidArea(new Dimension(0,5)));

        // Create the JTextField for entering the avatar URL & formatting
        avatarURL = new JTextField();
        ((AbstractDocument)avatarURL.getDocument()).setDocumentFilter(new LimitDocumentFilter(80));
        avatarURL.setBackground(LIGHTER_MID_GRAY);
        avatarURL.setForeground(WHITE);
        avatarURL.setBorder(BorderFactory.createLineBorder(DARK_GRAY, 2));
        avatarURL.setFont(new Font("Calibri",Font.PLAIN,20));
        avatarURL.setHorizontalAlignment(JTextField.CENTER);

        // Create the JScrollPane for the avatar URL field
        JScrollPane textScroller = new JScrollPane(avatarURL);
        textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScroller.setBorder(BorderFactory.createEmptyBorder());
        textScroller.setBackground(LIGHTER_MID_GRAY);
        textScroller.setPreferredSize(new Dimension(0,35));

        // Add text scroller to the main JPanel
        mainField.add(textScroller);

        return mainField;
    }

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
        label.setPreferredSize(new Dimension(0, 50));
        field.add(label);
        field.add(Box.createRigidArea(new Dimension(0,5)));

        // Fetch the list of channels in the server
        try {
            net.dv8tion.jda.api.entities.Webhook webhook = WebhookGUI.GUI.BOT.retrieveWebhookById(id).complete();

            long guildID = webhook.getGuild().getIdLong();
            Guild g = WebhookGUI.GUI.BOT.getGuildById(guildID);

            if (g == null)
                throw new NullPointerException("Guild with ID " + guildID + " is null!");
            List<String> tempList = g.getTextChannels().stream().map(channel -> channel.getName() + ":" + channel.getId()).collect(Collectors.toList());
            String[] channels = new String[tempList.size()];

            // Create the JComboBox for the channels, and formatting
            channelIDField = new JComboBox<>(tempList.toArray(channels));
            channelIDField.setBackground(LIGHTER_MID_GRAY);
            channelIDField.setForeground(WHITE);
            channelIDField.setBorder(null);
            channelIDField.setFont(new Font("Calibri", Font.PLAIN, 20));
            channelIDField.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
            channelIDField.setSelectedItem(IntStream.range(0, channels.length)
                    .filter(i -> channels[i].contains(webhook.getChannel().getId()))
                    .mapToObj(i -> channels[i])
                    .collect(Collectors.toList())
                    .get(0));
            JLabel comboLabel = ((JLabel) channelIDField.getRenderer());
            comboLabel.setHorizontalAlignment(JLabel.CENTER);

            // TODO Fix scroll wheel setting its size based on options currently not selected
            // Create the JScrollPane for the combo box
            JScrollPane textScroller = new JScrollPane(channelIDField);
            textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
            textScroller.setBackground(LIGHTER_MID_GRAY);
            textScroller.setBorder(BorderFactory.createLineBorder(DARK_GRAY, 2));
            textScroller.setPreferredSize(new Dimension(0, 35));

            standardizeScrollbar(textScroller.getVerticalScrollBar());
            standardizeScrollbar(textScroller.getHorizontalScrollBar());

            // Add text scroller to main panel
            field.add(textScroller);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Could not retrieve the Webhook. Please try again");
            field = null;
        }

        return field;
    }

    /**
     * Creates the {@link JPanel} which houses the field for entering the message contents, the {@link JButton} for sending the message, and a
     * {@link JButton} for closing the {@link WebhookConsole} {@link JFrame}
     *
     * @return A {@link JPanel}
     */
    @NotNull
    private JPanel message() {
        // Create main JPanel & formatting
        JPanel mainMessagePanel = new JPanel();
        mainMessagePanel.setOpaque(false);
        mainMessagePanel.setLayout(new BoxLayout(mainMessagePanel, BoxLayout.PAGE_AXIS));

        // Create the JLabel for the field's title, and formatting
        JLabel messageTitle = new JLabel("Message");
        messageTitle.setFont(new Font("Calibri",Font.BOLD,36));
        messageTitle.setOpaque(false);
        messageTitle.setForeground(WHITE);
        messageTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMessagePanel.add(messageTitle);
        mainMessagePanel.add(Box.createRigidArea(new Dimension(0,5)));

        // Create the JTextArea for entering the message's contents, and formatting
        JTextArea messageBox = new JTextArea();
        messageBox.setBackground(LIGHTER_MID_GRAY);
        messageBox.setForeground(WHITE);
        messageBox.setBorder(BorderFactory.createLineBorder(DARK_GRAY, 2));
        messageBox.setFont(new Font("Calibri",Font.PLAIN,18));
        messageBox.setLineWrap(true);
        messageBox.setWrapStyleWord(true);
        ((AbstractDocument)messageBox.getDocument()).setDocumentFilter(new LimitDocumentFilter(2000));

        // Add JScrollPane to the messageBox
        JScrollPane messageScroller = new JScrollPane(messageBox);
        messageScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageScroller.setBorder(BorderFactory.createEmptyBorder());
        messageScroller.setBackground(LIGHTER_MID_GRAY);

        // Create panel for the message field
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(DARK_GRAY);
        messagePanel.setLayout(new GridBagLayout());

        // Create the JButton for sending the message, and formatting
        JButton sendMessage = new JButton("Send Message");
        sendMessage.setBackground(BLURPLE);
        sendMessage.setForeground(WHITE);
        sendMessage.setFont(new Font("Calibri",Font.BOLD,40));
        sendMessage.setBorder(new RoundedBorder(Color.BLACK, 0, 16));
        sendMessage.setFocusable(false);
        setHoverBrightnessChange(sendMessage, .25f);

        // Add action listener for once the button is pressed
        sendMessage.addActionListener(event -> {
            try {
                new Thread(() -> {
                    // Update the webhooks channel
                    Object selectedItem = channelIDField.getSelectedItem();
                    if (selectedItem != null) {
                        TextChannel channel = WebhookGUI.GUI.BOT.getTextChannelById(selectedItem.toString().split(":")[1]);
                        if (channel != null)
                            WebhookGUI.GUI.BOT.retrieveWebhookById(id)
                                    .flatMap(webhook -> webhook.getManager().setChannel(channel)).complete();
                    }

                    // Collect username and message
                    String username = usernameBox.getText().length() > 0 ? usernameBox.getText() : null;
                    String message = messageBox.getText();

                    // Check message length
                    if (message.length() == 0)
                        JOptionPane.showMessageDialog(this, "Message must be longer than 0 characters.");
                    else
                        try {
                            // Sends message if sending the message was successful
                            // TODO setting to disable
                            if (DiscordAPI.sendMessage(username, avatarURL.getText().length() > 0 ? avatarURL.getText() : null, message, id, token))
                                JOptionPane.showMessageDialog(this, "Your message was sent!");
                            else
                                JOptionPane.showMessageDialog(this, "Your message could not be sent! Consider checking the " +
                                        "status of the bot and the webhook you are attempting to use.");

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, e.getMessage());
                        }
                }).start();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An error was encountered trying to send your message. Please try again: " + e.getMessage());
            }
        });

        // Create GBC for formatting and adding the messageScroller
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        messagePanel.add(messageScroller, gbc);

        // Create the panel for the buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(NOT_QUITE_BLACK);

        // Update constraints and add the "Send Message" button
        gbc.weighty = 1;
        gbc.weightx = .9;
        gbc.insets = new Insets(20,50, 20, 5);
        buttonPanel.add(sendMessage, gbc);

        // Update constraints and add the cancel button
        gbc.weightx = .1;
        gbc.insets = new Insets(20,5, 20, 50);
        gbc.gridx = 1;
        buttonPanel.add(cancel(this), gbc);

        // Add button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);

        // Add the message field to the main JPanel
        mainMessagePanel.add(messagePanel);

        return mainMessagePanel;
    }
}
