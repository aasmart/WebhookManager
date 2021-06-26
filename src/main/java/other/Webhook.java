package other;

import com.iwebpp.crypto.TweetNaclFast;
import guis.JPanelEssentials;
import guis.RoundedBorder;
import guis.WebhookConsole;
import guis.WebhookCreateConsole;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;

public class Webhook extends JPanelEssentials {
    private final String name;
    private final String token;
    private final String id;
    private final String channelID;

    public Webhook(String name, String token, String id, String channelID) {
        this.name = name;
        this.token = token;
        this.id = id;
        this.channelID = channelID;
    }

    public static boolean createWebhook(String name, File avatar, String channelID) {
        TextChannel channel;
        try {
             channel = WebhookGUI.GUI.BOT.getTextChannelById(channelID);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Invalid channel value.");
            return false;
        }

        if(channel == null) {
            JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Cannot find the specified channel.");
            return false;
        }

        try {
            channel.createWebhook(name).queue(webhook -> {
                try {
                    webhook.getManager().setAvatar(Icon.from(avatar)).queue();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, "Avatar couldn't be set.");
                }

                // TODO Make guild independent
                webhook.getGuild().retrieveWebhooks().queue(webhooks ->
                        WebhookGUI.GUI.MAIN_CONSOLE.populateList(WebhookGUI.GUI.MAIN_CONSOLE.list, webhooks.stream().map(hook -> new Webhook(hook.getName(), hook.getToken(), hook.getId(), hook.getChannel().getId())).collect(Collectors.toList()))
                );
            }, fail ->
                JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, fail)
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(WebhookGUI.GUI.MAIN_CONSOLE, e.getMessage());
        }
        return true;
    }

    public JPanel createPanel() {
        JPanel webhookPanel = new JPanel();
        webhookPanel.setLayout(new GridBagLayout());
        webhookPanel.setBackground(DARK_GRAY);
        addHoverBrightnessChange(webhookPanel, .25f);

        AbstractBorder roundedBorder = new RoundedBorder(NOT_QUITE_BLACK,2,16, 0);

        webhookPanel.setBorder(roundedBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = .65;

        webhookPanel.add(name(), gbc);

        gbc.weightx = .35;
        gbc.insets = new Insets(0,10,0,0);
        webhookPanel.add(channel(), gbc);

        gbc.weightx = .0125;
        webhookPanel.add(load(), gbc);
        webhookPanel.add(delete(), gbc);

        return webhookPanel;
    }

    @SuppressWarnings("DuplicatedCode")
    private JPanel name() {
        JPanel nameInfoPanel = new JPanel();
        nameInfoPanel.setLayout(new BoxLayout(nameInfoPanel, BoxLayout.PAGE_AXIS));
        nameInfoPanel.setOpaque(false);

        JLabel title = new JLabel("Name");
        title.setForeground(GRAY);
        title.setOpaque(false);
        title.setFont(new Font("Calibri",Font.BOLD,24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField name = new JTextField(this.name);
        name.setEnabled(false);
        name.setOpaque(false);
        name.setFont(new Font("Calibri",Font.PLAIN,20));
        name.setHorizontalAlignment(JTextField.CENTER);
        name.setBorder(BorderFactory.createEmptyBorder());
        name.setForeground(WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        JScrollPane nameScroll = new JScrollPane(name);
        nameScroll.setBorder(BorderFactory.createEmptyBorder());
        nameScroll.setOpaque(false);
        nameScroll.getViewport().setOpaque(false);
        nameScroll.setPreferredSize(new Dimension(0, 100));
        nameScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

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

        nameInfoPanel.add(Box.createVerticalGlue());
        nameInfoPanel.add(title);
        nameInfoPanel.add(Box.createRigidArea(new Dimension(0,3)));
        nameInfoPanel.add(nameScroll);
        nameInfoPanel.add(Box.createVerticalGlue());

        return nameInfoPanel;
    }

    @SuppressWarnings("DuplicatedCode")
    private JPanel channel() {
        JPanel channelInfoPanel = new JPanel();
        channelInfoPanel.setLayout(new BoxLayout(channelInfoPanel, BoxLayout.PAGE_AXIS));
        channelInfoPanel.setOpaque(false);

        JLabel title = new JLabel("Channel");
        title.setForeground(GRAY);
        title.setOpaque(false);
        title.setFont(new Font("Calibri",Font.BOLD,24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        TextChannel textChannel = WebhookGUI.GUI.BOT.getTextChannelById(this.channelID);
        JTextField channel = new JTextField(textChannel != null ? textChannel.getName() : "NULL");
        channel.setEnabled(false);
        channel.setOpaque(false);
        channel.setFont(new Font("Calibri",Font.PLAIN,20));
        channel.setHorizontalAlignment(JLabel.CENTER);
        channel.setBorder(BorderFactory.createEmptyBorder());
        channel.setForeground(WHITE);

        JScrollPane channelScroll = new JScrollPane(channel);
        channelScroll.setBorder(BorderFactory.createEmptyBorder());
        channelScroll.setOpaque(false);
        channelScroll.getViewport().setOpaque(false);
        channelScroll.setPreferredSize(new Dimension(0, 100));
        channelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        channelInfoPanel.add(Box.createVerticalGlue());
        channelInfoPanel.add(title);
        channelInfoPanel.add(Box.createRigidArea(new Dimension(0,3)));
        channelInfoPanel.add(channelScroll);
        channelInfoPanel.add(Box.createVerticalGlue());

        return channelInfoPanel;
    }

    private JButton load() {
        JButton load = new JButton();
        load.setBackground(BLURPLE);
        load.setForeground(WHITE);
        load.setFont(new Font("Calibri",Font.PLAIN,48));
        load.setFocusable(false);
        addHoverBrightnessChange(load, .25f);
        load.setBorder(new RoundedBorder(Color.RED,0,16, 0));

        try {
            URL resource = getClass().getResource("/arrowright.png");
            if(resource == null)
                throw new NullPointerException("Could not get resource: arrowright.png");
            Image img = ImageIO.read(resource);
            load.setIcon(new ImageIcon(img.getScaledInstance( 56, 56,  java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        load.addActionListener(event -> {
            WebhookGUI.GUI.MAIN_CONSOLE.setEnabled(false);
            new WebhookConsole(id, token);
        });
        return load;
    }

    @SuppressWarnings("DuplicatedCode")
    private JPanel delete() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(DARK_GRAY);
        buttonPanel.setLayout(new GridBagLayout());

        JButton deleteButton = new JButton();
        deleteButton.setBackground(RED);
        deleteButton.setForeground(WHITE);
        deleteButton.setBorder(new RoundedBorder(MID_GRAY,0,16, 0));
        deleteButton.setFont(new Font("Calibri",Font.BOLD,40));
        addHoverBrightnessChange(deleteButton, .25f);
        deleteButton.setFocusable(false);

        try {
            URL resource = WebhookCreateConsole.class.getResource("/xicon.png");
            if(resource == null)
                throw new NullPointerException("Could not get resource: xicon.png");
            Image img = ImageIO.read(resource);
            deleteButton.setIcon(new ImageIcon(img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        deleteButton.addActionListener(event ->
            WebhookGUI.GUI.BOT.retrieveWebhookById(id).queue(webhook -> {
                webhook.delete().queue();

                webhook.getGuild().retrieveWebhooks().queue(webhooks ->
                        WebhookGUI.GUI.MAIN_CONSOLE.populateList(WebhookGUI.GUI.MAIN_CONSOLE.list, webhooks.stream().map(hook -> new Webhook(hook.getName(), hook.getToken(), hook.getId(), hook.getChannel().getId())).collect(Collectors.toList()))
                );
            })
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        buttonPanel.add(deleteButton, gbc);

        return buttonPanel;
    }

}
