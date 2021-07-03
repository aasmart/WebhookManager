package com.smart.manager.guiComponents.popups;

import com.smart.manager.events.Startup;
import com.smart.manager.guiComponents.JFrameEssentials;
import com.smart.manager.guiComponents.RoundedBorder;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * A {@link JFrame} pop up for telling the user the bot is in no servers
 */
// TODO add option for changing bot token
public class NoGuildsPopUp extends JFrameEssentials {
    /**
     * Creates the pop up
     */
    public NoGuildsPopUp() {
        // Set basic JFrame settings
        setTitle("The Bot is Currently in No Guilds!");
        setSize(500, 350);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create JPanel for explaining the popup
        JPanel explanation = new JPanel();
        explanation.setBackground(NOT_QUITE_BLACK);
        explanation.setLayout(new GridBagLayout());

        // Create text pane for the explanation text
        JTextPane text = new JTextPane();
        text.setText("In order to use the webhook console your bot must be in at least one server!");
        text.setBackground(NOT_QUITE_BLACK);
        text.setEditable(false);

        // Text Pane Attributes
        SimpleAttributeSet attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(attribs, WHITE);
        StyleConstants.setFontSize(attribs, 36);
        StyleConstants.setBold(attribs, true);
        StyleConstants.setFontFamily(attribs, "Calibri");
        text.setParagraphAttributes(attribs, true);

        // Create JButton for attempting to refresh if the user added the bot to guilds
        JButton refresh = new JButton("Refresh");
        refresh.setBackground(BLURPLE);
        refresh.setBorder(new RoundedBorder(Color.BLACK, 0, 16));
        refresh.setForeground(WHITE);
        refresh.setFont(new Font("Calibri",Font.BOLD,20));
        setHoverBrightnessChange(refresh, .25f);

        // Add action listener for refreshing on click
        refresh.addActionListener(action -> {
            this.dispose();
            Startup.attemptStartup();
        });

        // Create JButton for attempting to refresh if the user added the bot to guilds
        JButton changeToken = new JButton("Change Token");
        changeToken.setBackground(BLURPLE);
        changeToken.setBorder(new RoundedBorder(Color.BLACK, 0, 16));
        changeToken.setForeground(WHITE);
        changeToken.setFont(new Font("Calibri",Font.BOLD,20));
        setHoverBrightnessChange(changeToken, .25f);

        // Add action listener for refreshing on click
        changeToken.addActionListener(action -> {
            this.dispose();
            new TokenPopUp("Change Token", NoGuildsPopUp.class);
        });

        // Create GBC for formatting and add the text to the explanation panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = .7;
        gbc.weightx = 1;
        explanation.add(text, gbc);

        // Update constraints add add refresh button
        gbc.weighty = .15;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 20, 20);
        explanation.add(refresh, gbc);

        // Change token button
        gbc.gridy = 2;
        explanation.add(changeToken, gbc);

        // Add explanation the the JFrame
        add(explanation);
        setVisible(true);
        getContentPane().repaint();
    }
}
