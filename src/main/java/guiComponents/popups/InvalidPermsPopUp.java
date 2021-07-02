package guiComponents.popups;

import events.Startup;
import guiComponents.JFrameEssentials;
import guiComponents.RoundedBorder;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * A {@link JFrame} pop up for telling the user the bot is in no servers
 */
// TODO add option for changing bot token
public class InvalidPermsPopUp extends JFrameEssentials {
    /**
     * Creates the pop up
     */
    public InvalidPermsPopUp(String message) {
        // Set basic JFrame settings
        setTitle("Insufficient Permissions!");
        setSize(500, 250);
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
        text.setText(message + ". Give the bot the given permission and hit refresh.");
        text.setBackground(NOT_QUITE_BLACK);
        text.setEditable(false);

        // Setup scroll pane
        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setBackground(NOT_QUITE_BLACK);
        scrollPane.setBorder(null);

        JScrollBar horizontalBar = scrollPane.getHorizontalScrollBar();
        horizontalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BLURPLE;
                this.trackColor = DARK_GRAY;
                this.thumbDarkShadowColor = DARK_GRAY;
            }
        });

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BLURPLE;
                this.trackColor = DARK_GRAY;
                this.thumbDarkShadowColor = DARK_GRAY;
            }
        });

        // Text Pane Attributes
        SimpleAttributeSet attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(attribs, WHITE);
        StyleConstants.setFontSize(attribs, 20);
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

        // Create GBC for formatting and add the text to the explanation panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = .8;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 20, 20, 20);
        explanation.add(scrollPane, gbc);

        // Update constraints add add refresh button
        gbc.weighty = .2;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 20, 20);
        explanation.add(refresh, gbc);

        // Add explanation the the JFrame
        add(explanation);
        setVisible(true);
    }
}
