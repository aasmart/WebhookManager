package guis;

import events.Startup;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class NoGuildsPopUp extends JPanelEssentials{
    public NoGuildsPopUp() {
        setTitle("The Bot is Currently in No Guilds!");
        setSize(500, 250);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel explanation = new JPanel();
        explanation.setBackground(NOT_QUITE_BLACK);
        explanation.setLayout(new GridBagLayout());

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

        JButton refresh = new JButton("Refresh");
        refresh.setBackground(BLURPLE);
        refresh.setBorder(BorderFactory.createEmptyBorder());
        refresh.setForeground(WHITE);
        refresh.setFont(new Font("Calibri",Font.BOLD,24));

        refresh.addActionListener(action -> {
            this.dispose();
            Startup.attemptStartup();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = .8;
        gbc.weightx = 1;

        explanation.add(text, gbc);
        gbc.weighty = .2;
        gbc.gridy = 1;

        explanation.add(refresh, gbc);
        add(explanation);
        setVisible(true);
    }
}
