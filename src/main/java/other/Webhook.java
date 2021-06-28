package other;

import guiComponents.JFrameEssentials;
import guiComponents.RoundedBorder;
import guiComponents.guis.WebhookConsole;
import guiComponents.guis.WebhookCreateConsole;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 *
 */
public class Webhook extends JFrameEssentials {
    /**
     * The name of the Webhook
     */
    private final String name;

    /**
     * The Webhook's token
     */
    private final String token;

    /**
     * The Webhook's ID
     */
    private final String id;

    /**
     * The ID of the Webhook's channel
     */
    private final String channelID;

    public Webhook(String name, String token, String id, String channelID) {
        this.name = name;
        this.token = token;
        this.id = id;
        this.channelID = channelID;
    }

    /**
     * Creates a Webhook
     *
     * @param name The name of the webhook
     * @param avatar The avatar of the webhook
     * @param channelID The channel where the webhook is being created in
     * @return True if creating was successful
     */
    public static boolean createWebhook(String name, File avatar, String channelID) {
        // Attempt to get the text channel
        TextChannel channel;
        try {
             channel = WebhookGUI.GUI.BOT.getTextChannelById(channelID);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Invalid channel value.");
            return false;
        }

        // Channel null check
        if(channel == null) {
            JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Cannot find the specified channel.");
            return false;
        }

        // Attempt to create the webhook
        try {
            channel.createWebhook(name).queue(webhook -> {
                try {
                    webhook.getManager().setAvatar(Icon.from(avatar)).queue();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Avatar couldn't be set.");
                }

                WebhookGUI.GUI.MAIN_CONSOLE.populateList(WebhookGUI.GUI.MAIN_CONSOLE.tabGuildIDMap.get(WebhookGUI.GUI.MAIN_CONSOLE.guildPanel.getSelectedIndex()));
            }, fail ->
                JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, fail)
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, e.getMessage());
        }
        return true;
    }

    /**
     * Creates the {@link JPanel} for the {@link Webhook} containing all of its information
     *
     * @return A {@link JPanel}
     */
    public JPanel createPanel() {
        // Create the main JPanel
        JPanel webhookPanel = new JPanel();
        webhookPanel.setLayout(new GridBagLayout());
        webhookPanel.setBackground(DARK_GRAY);
        addHoverBrightnessChange(webhookPanel, .25f);
        webhookPanel.setBorder(new RoundedBorder(NOT_QUITE_BLACK,2,16));

        // Create GBC for formatting
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = .65;

        // Add the Webhook name field
        webhookPanel.add(name(), gbc);

        // Update constraints add the Webhook channel field
        gbc.weightx = .35;
        gbc.insets = new Insets(0,10,0,0);
        webhookPanel.add(channel(), gbc);

        // Update constraints and add the load and delete buttons
        gbc.weightx = .0125;
        webhookPanel.add(load(), gbc);
        webhookPanel.add(delete(), gbc);

        return webhookPanel;
    }

    /**
     * Create the {@link JPanel} for the {@link Webhook}'s name
     *
     * @return A {@link JPanel}
     */
    @SuppressWarnings("DuplicatedCode")
    private JPanel name() {
        // The main JPanel
        JPanel nameInfoPanel = new JPanel();
        nameInfoPanel.setLayout(new BoxLayout(nameInfoPanel, BoxLayout.PAGE_AXIS));
        nameInfoPanel.setOpaque(false);

        // Create the title for the name field
        JLabel title = new JLabel("Name");
        title.setForeground(GRAY);
        title.setOpaque(false);
        title.setFont(new Font("Calibri",Font.BOLD,24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Creates the JTextField for the Webhook's name
        JTextField name = new JTextField(this.name);
        name.setEnabled(false);
        name.setOpaque(false);
        name.setFont(new Font("Calibri",Font.PLAIN,20));
        name.setHorizontalAlignment(JTextField.CENTER);
        name.setBorder(BorderFactory.createEmptyBorder());
        name.setForeground(WHITE);

        // Create GBC for formatting
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        // Create the JScrollPane for the name field
        JScrollPane nameScroll = new JScrollPane(name);
        nameScroll.setBorder(BorderFactory.createEmptyBorder());
        nameScroll.setOpaque(false);
        nameScroll.getViewport().setOpaque(false);
        nameScroll.setPreferredSize(new Dimension(0, 100));
        nameScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // JScrollBar formatting for the name field
        JScrollBar horizontalBar = nameScroll.getHorizontalScrollBar();
        horizontalBar.setPreferredSize(new Dimension(0, 10));
        horizontalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BLURPLE;
                this.trackColor = NOT_QUITE_BLACK;
                this.thumbDarkShadowColor = NOT_QUITE_BLACK;
            }
        });

        // Add fields to the main JPanel
        nameInfoPanel.add(Box.createVerticalGlue());
        nameInfoPanel.add(title);
        nameInfoPanel.add(Box.createRigidArea(new Dimension(0,3)));
        nameInfoPanel.add(nameScroll);
        nameInfoPanel.add(Box.createVerticalGlue());

        return nameInfoPanel;
    }

    /**
     * Create the {@link JPanel} for the channel name field
     *
     * @return A {@link JPanel}
     */
    @SuppressWarnings("DuplicatedCode")
    private JPanel channel() {
        // Create the main JPanel
        JPanel channelInfoPanel = new JPanel();
        channelInfoPanel.setLayout(new BoxLayout(channelInfoPanel, BoxLayout.PAGE_AXIS));
        channelInfoPanel.setOpaque(false);

        // Create the JLabel for the title
        JLabel title = new JLabel("Channel");
        title.setForeground(GRAY);
        title.setOpaque(false);
        title.setFont(new Font("Calibri",Font.BOLD,24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Get the text channel and name
        TextChannel textChannel = WebhookGUI.GUI.BOT.getTextChannelById(this.channelID);

        // Create the JTextField for the channel's name
        JTextField channel = new JTextField(textChannel != null ? textChannel.getName() : "NULL");
        channel.setEnabled(false);
        channel.setOpaque(false);
        channel.setFont(new Font("Calibri",Font.PLAIN,20));
        channel.setHorizontalAlignment(JLabel.CENTER);
        channel.setBorder(BorderFactory.createEmptyBorder());
        channel.setForeground(WHITE);

        // Create the JScrollBar for the channel's name
        JScrollPane channelScroll = new JScrollPane(channel);
        channelScroll.setBorder(BorderFactory.createEmptyBorder());
        channelScroll.setOpaque(false);
        channelScroll.getViewport().setOpaque(false);
        channelScroll.setPreferredSize(new Dimension(0, 100));
        channelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // Update formatting for channelScroll scroll bars
        JScrollBar horizontalBar = channelScroll.getHorizontalScrollBar();
        horizontalBar.setPreferredSize(new Dimension(0, 10));
        horizontalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BLURPLE;
                this.trackColor = NOT_QUITE_BLACK;
                this.thumbDarkShadowColor = NOT_QUITE_BLACK;
            }
        });

        // Create GBC for formatting
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        // Add various fields to main panel
        channelInfoPanel.add(Box.createVerticalGlue());
        channelInfoPanel.add(title);
        channelInfoPanel.add(Box.createRigidArea(new Dimension(0,3)));
        channelInfoPanel.add(channelScroll);
        channelInfoPanel.add(Box.createVerticalGlue());

        return channelInfoPanel;
    }

    /**
     * Create the {@link JButton} for "loading" the {@link Webhook}, which calls the {@link WebhookConsole} constructor
     *
     * @return A {@link JButton}
     */
    private JButton load() {
        // Create the load button
        JButton load = new JButton();
        load.setBackground(BLURPLE);
        load.setForeground(WHITE);
        load.setFont(new Font("Calibri",Font.PLAIN,48));
        load.setFocusable(false);
        addHoverBrightnessChange(load, .25f);
        load.setBorder(new RoundedBorder(Color.RED,0,16));

        // Get the button's icon
        try {
            URL resource = getClass().getResource("/arrowright.png");
            if(resource == null)
                throw new NullPointerException("Could not get resource: arrowright.png");
            Image img = ImageIO.read(resource);
            load.setIcon(new ImageIcon(img.getScaledInstance( 56, 56,  java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add action listener for managing the webhook on click
        load.addActionListener(event -> {
            WebhookGUI.GUI.MAIN_CONSOLE.setEnabled(false);
            new WebhookConsole(id, token);
        });
        return load;
    }

    /**
     * Create the {@link JButton} for deleting the {@link Webhook}
     *
     * @return A {@link JPanel}
     */
    @SuppressWarnings("DuplicatedCode")
    private JPanel delete() {
        // THe main JPanel for the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(DARK_GRAY);
        buttonPanel.setLayout(new GridBagLayout());

        // Create the JButton
        JButton deleteButton = new JButton();
        deleteButton.setBackground(RED);
        deleteButton.setForeground(WHITE);
        deleteButton.setBorder(new RoundedBorder(MID_GRAY,0,16));
        deleteButton.setFont(new Font("Calibri",Font.BOLD,40));
        addHoverBrightnessChange(deleteButton, .25f);
        deleteButton.setFocusable(false);

        // Get the JButton's icon
        try {
            URL resource = WebhookCreateConsole.class.getResource("/xicon.png");
            if(resource == null)
                throw new NullPointerException("Could not get resource: xicon.png");
            Image img = ImageIO.read(resource);
            deleteButton.setIcon(new ImageIcon(img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add action listener for deleting the webhook on click
        deleteButton.addActionListener(event ->
            WebhookGUI.GUI.BOT.retrieveWebhookById(id).queue(webhook -> {
                webhook.delete().queue();
                WebhookGUI.GUI.MAIN_CONSOLE.populateList(WebhookGUI.GUI.MAIN_CONSOLE.tabGuildIDMap.get(WebhookGUI.GUI.MAIN_CONSOLE.guildPanel.getSelectedIndex()));
            })
        );

        // Create GBC for formatting
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        // Add the delete button the main panel
        buttonPanel.add(deleteButton, gbc);

        return buttonPanel;
    }

}
