package guis.popups;

import events.Startup;
import guis.JPanelEssentials;
import guis.RoundedBorder;
import other.WebhookGUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class TokenPopUp extends JPanelEssentials {
    public TokenPopUp(String titleText) {
        setTitle(titleText);
        setSize(600, 350);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel explanation = new JPanel();
        explanation.setBackground(NOT_QUITE_BLACK);
        explanation.setLayout(new GridBagLayout());

        JTextPane titleTextPane = new JTextPane();
        titleTextPane.setText(titleText);
        titleTextPane.setBackground(NOT_QUITE_BLACK);
        titleTextPane.setEditable(false);

        // Text Pane Attributes
        SimpleAttributeSet attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(attribs, WHITE);
        StyleConstants.setFontSize(attribs, 36);
        StyleConstants.setBold(attribs, true);
        StyleConstants.setFontFamily(attribs, "Calibri");
        titleTextPane.setParagraphAttributes(attribs, true);

        // The body text
        JTextPane body = new JTextPane();
        body.setText("In order to use this application a bot token must be provided. This token will be stored locally " +
                "on your computer and WILL ONLY be used by this application to load your bot to manage your webhooks. If you do not trust this " +
                "application do not insert your bot token. Insert your bot token in the gray box below:");
        body.setOpaque(false);
        body.setEditable(false);
        attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(attribs, WHITE);
        StyleConstants.setFontSize(attribs, 16);
        StyleConstants.setFontFamily(attribs, "Calibri");
        body.setParagraphAttributes(attribs, true);

        // The token field
        JTextField tokenField = new JTextField();
        tokenField.setBackground(GRAY);
        tokenField.setForeground(NOT_QUITE_BLACK);
        tokenField.setFont(new Font("Calibri",Font.PLAIN,20));
        tokenField.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane tokenScroll = new JScrollPane(tokenField);
        tokenScroll.setBorder(BorderFactory.createLineBorder(MID_GRAY, 5));

        JScrollBar horizontalBar = tokenScroll.getHorizontalScrollBar();
        //horizontalBar.setPreferredSize(new Dimension(0, 10));
        horizontalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BLURPLE;
                this.trackColor = DARK_GRAY;
                this.thumbDarkShadowColor = NOT_QUITE_BLACK;
            }
        });

        JScrollBar verticalBar = tokenScroll.getVerticalScrollBar();
        //horizontalBar.setPreferredSize(new Dimension(0, 10));
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BLURPLE;
                this.trackColor = DARK_GRAY;
                this.thumbDarkShadowColor = NOT_QUITE_BLACK;
            }
        });

        JButton submit = new JButton("Submit Token");
        submit.setBackground(BLURPLE);
        submit.setBorder(new RoundedBorder(Color.BLACK, 0, 16, 0));
        submit.setForeground(WHITE);
        submit.setFont(new Font("Calibri",Font.BOLD,20));
        addHoverBrightnessChange(submit, .25f);

        submit.addActionListener(action -> {
            String token = tokenField.getText();
            if(token.length() > 0) {
                this.dispose();
                if(WebhookGUI.writeToken(tokenField.getText()))
                    WebhookGUI.GUI = new WebhookGUI();
            } else
                JOptionPane.showMessageDialog(this, "Must have a token!");
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = .2;
        gbc.weightx = 1;
        explanation.add(titleTextPane, gbc);

        gbc.weighty = .25;
        gbc.gridy = 1;
        gbc.insets = new Insets(3, 10, 0, 10);
        explanation.add(body, gbc);

        gbc.weighty = .45;
        gbc.gridy = 2;
        gbc.insets = new Insets(3, 20, 20, 20);
        explanation.add(tokenScroll, gbc);

        gbc.weighty = .1;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 20, 20, 20);
        explanation.add(submit, gbc);

        add(explanation);

        UIManager.put("OptionPane.background", NOT_QUITE_BLACK);
        UIManager.put("Panel.background", NOT_QUITE_BLACK);
        UIManager.put("OptionPane.messageForeground", WHITE);

        setVisible(true);
    }
}
