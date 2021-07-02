package guiComponents.popups;

import guiComponents.JFrameEssentials;
import guiComponents.RoundedBorder;
import guiComponents.settings.ManagerSettings;
import other.WebhookGUI;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

/**
 * A popup for telling the user that the application is missing a (valid) bot token
 */
public class TokenPopUp extends JFrameEssentials {
    /**
     * Creates a TokenPopUp with a cancel {@link JButton}
     * @param titleText The text to set the title of the popup to
     * @param returnTo The JFrame to return to when the window is closed
     */
    public <G extends JFrameEssentials> TokenPopUp(String titleText, Class<G> returnTo) {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    returnTo.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
            }
        });
        createPopUp(titleText, true);
    }

    /**
     * Creates the token pop up
     *
     * @param titleText The text to set the title of the popup to
     */
    public TokenPopUp(String titleText) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createPopUp(titleText, false);
    }

    /**
     * Creates the popup
     * @param titleText The text to set the title of the popup to
     * @param hasCancelButton If the popup has the cancel button
     */
    private void createPopUp(String titleText, boolean hasCancelButton) {
        // Basic JFrame formatting
        setTitle(titleText);
        setSize(600, 350);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);

        // Create explanation JPanel
        JPanel explanation = new JPanel();
        explanation.setBackground(NOT_QUITE_BLACK);
        explanation.setLayout(new GridBagLayout());

        // Create the text for explaining the popup
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

        // The body text & formatting
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

        // Create scrolling for token field
        JScrollPane tokenScroll = new JScrollPane(tokenField);
        tokenScroll.setBorder(BorderFactory.createLineBorder(MID_GRAY, 5));

        // Set scroll bar formatting
        standardizeScrollbar(tokenScroll.getHorizontalScrollBar());
        standardizeScrollbar(tokenScroll.getVerticalScrollBar());

        // Create the JButton for submitting the token
        JButton submit = new JButton("Submit Token");
        submit.setBackground(BLURPLE);
        submit.setBorder(new RoundedBorder(Color.BLACK, 0, 16));
        submit.setForeground(WHITE);
        submit.setFont(new Font("Calibri",Font.BOLD,20));
        setHoverBrightnessChange(submit, .25f);

        // Add action listener for submitting the token on click
        submit.addActionListener(action -> {
            String token = tokenField.getText();
            if(token.length() > 0) {
                // Remove the active window listener because it calls on close and that's not what I want
                if(getWindowListeners().length > 0)
                    this.removeWindowListener(this.getWindowListeners()[0]);
                this.dispose();
                if(WebhookGUI.writeToken(tokenField.getText())) {
                    if(WebhookGUI.GUI.BOT != null)
                        WebhookGUI.GUI.BOT.shutdownNow();
                    WebhookGUI.settings = ManagerSettings.compileSettings();
                    WebhookGUI.GUI = new WebhookGUI();
                }
            } else
                JOptionPane.showMessageDialog(this, "Must have a token!");
        });

        // Create "Cancel Button" if the Action Listener exists
        JButton cancel = null;
        if(hasCancelButton) {
            cancel = new JButton("Cancel");
            cancel.setBackground(RED);
            cancel.setBorder(new RoundedBorder(Color.BLACK, 0, 16));
            cancel.setForeground(WHITE);
            cancel.setFont(new Font("Calibri",Font.BOLD,20));
            setHoverBrightnessChange(cancel, .25f);

            cancel.addActionListener(action -> dispose());
        }

        // Create GBC for formatting and add the title text to the explanation JPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = .2;
        gbc.weightx = 1;
        explanation.add(titleTextPane, gbc);

        // Updated constraints and add the body text to the explanation JPanel
        gbc.weighty = .25;
        gbc.gridy = 1;
        gbc.insets = new Insets(3, 10, 0, 10);
        explanation.add(body, gbc);

        // Update constraints and add the token field
        gbc.weighty = .45;
        gbc.gridy = 2;
        gbc.insets = new Insets(3, 20, 20, 20);
        explanation.add(tokenScroll, gbc);

        // JPanel for the buttons
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setBackground(NOT_QUITE_BLACK);

        // Updated constraints add add the submit/cancel JButtons
        gbc.weighty = 1;
        gbc.weightx = .8;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 20);
        buttonsPanel.add(submit, gbc);

        if(cancel != null) {
            gbc.weightx = .2;
            gbc.insets = new Insets(0, 0, 0, 0);
            buttonsPanel.add(cancel, gbc);
        }

        // Add buttonPanel to explanation
        gbc.weighty = .1;
        gbc.weightx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 20, 20, 20);
        explanation.add(buttonsPanel, gbc);

        // Add the explanation JPanel to the JFrame
        add(explanation);

        // Update UI
        UIManager.put("OptionPane.background", NOT_QUITE_BLACK);
        UIManager.put("Panel.background", NOT_QUITE_BLACK);
        UIManager.put("OptionPane.messageForeground", WHITE);

        setVisible(true);
    }
}
