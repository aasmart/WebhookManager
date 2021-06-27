package guiComponents.guis;

import guiComponents.JFrameEssentials;
import guiComponents.LimitDocumentFilter;
import guiComponents.RoundedBorder;
import org.jetbrains.annotations.NotNull;
import other.DiscordAPI;
import other.WebhookGUI;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The console for managing {@link other.Webhook}s
 */
public class WebhookConsole extends JFrameEssentials {
    /**
     * The {@link JTextField} for housing the {@link other.Webhook}'s username
     */
    private JTextField usernameBox;

    /**
     * The {@link JTextField} for housing the {@link other.Webhook}'s avatar URL
     */
    private JTextField avatarURL;

    /**
     * The ID of the {@link other.Webhook} the user is currently managing
     */
    private final String id;

    /**
     * The token of the {@link other.Webhook} the user is currently managing
     */
    private final String token;

    /**
     * A {@link ScheduledExecutorService} for managing the change in color based on a success/failure in the send message button
     */
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    /**
     * The basic constructor for the {@link WebhookConsole} {@link JFrame}.
     *
     * @param id The ID of the {@link other.Webhook} the user is managing
     * @param token The token of the {@link other.Webhook} the user is managing
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
        add(userInputPanel(), BorderLayout.CENTER);

        // Create padding
        add(padding(NOT_QUITE_BLACK), BorderLayout.WEST);
        add(padding(NOT_QUITE_BLACK), BorderLayout.EAST);

        // Create listener for window closing event
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                WebhookGUI.GUI.MAIN_CONSOLE.populateList();
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
    @NotNull
    private JPanel userInputPanel() {
        // Create the main JPanel and formatting
        JPanel panel = new JPanel();
        panel.setBackground(DARK_GRAY);
        panel.setLayout(new GridBagLayout());

        // Create the JPanel that goes on the top row of the main JPanel
        JPanel subPanelTop = new JPanel();
        subPanelTop.setBackground(DARK_GRAY);
        subPanelTop.setLayout(new GridBagLayout());

        // Create GBC for formatting
        GridBagConstraints gbcSub = new GridBagConstraints();
        gbcSub.gridx = 0;
        gbcSub.gridy = 0;
        gbcSub.weightx = .75;
        gbcSub.weighty = 1;
        gbcSub.fill = GridBagConstraints.BOTH;
        gbcSub.insets = new Insets(40, 40, 40, 20);

        // Add the username field to the top sub JPanel
        subPanelTop.add(usernameField(), gbcSub);

        // Update constraints and add the avatar field to the top sub JPanel
        gbcSub.gridx = 1;
        gbcSub.weightx = .25;
        gbcSub.insets = new Insets(40, 20, 40, 40);
        subPanelTop.add(avatarURLField(), gbcSub);

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
     * Creates the field for entering the {@link other.Webhook}'s username when sending a message
     *
     * @return A {@link JPanel}
     */
    @NotNull
    private JPanel usernameField() {
        // Create JPanel for housing the JLabel/JTextField
        JPanel field = new JPanel();
        field.setBackground(DARK_GRAY);
        field.setLayout(new BoxLayout(field, BoxLayout.PAGE_AXIS));

        // Create the JLabel (Title)
        JLabel label = new JLabel("Username");
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.add(label);
        field.add(Box.createRigidArea(new Dimension(0,5)));

        // Create the JTextField for entering the Webhook's nickname
        usernameBox = new JTextField();
        ((AbstractDocument)usernameBox.getDocument()).setDocumentFilter(new LimitDocumentFilter(80));
        usernameBox.setBackground(GRAY);
        usernameBox.setForeground(NOT_QUITE_BLACK);
        usernameBox.setFont(new Font("Calibri",Font.PLAIN,20));
        usernameBox.setHorizontalAlignment(JTextField.CENTER);

        // Add the username text field to a JScrollPane
        JScrollPane textScroller = new JScrollPane(usernameBox);
        textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScroller.setBackground(GRAY);
        textScroller.setBorder(BorderFactory.createEmptyBorder());

        // Add the text scroller to the main JPanel
        field.add(textScroller);

        return field;
    }

    /**
     * Creates the field for entering the {@link other.Webhook}'s avatar for when sending a message
     *
     * @return A {@link JPanel}
     */
    @NotNull
    private JPanel avatarURLField() {
        // Create main JPanel & formatting
        JPanel mainField = new JPanel();
        mainField.setBackground(DARK_GRAY);
        mainField.setLayout(new BoxLayout(mainField, BoxLayout.PAGE_AXIS));

        // Create JLabel for title
        JLabel label = new JLabel("Avatar URL");
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainField.add(label);
        mainField.add(Box.createRigidArea(new Dimension(0,5)));

        // Create the JTextField for entering the avatar URL & formatting
        avatarURL = new JTextField();
        ((AbstractDocument)avatarURL.getDocument()).setDocumentFilter(new LimitDocumentFilter(80));
        avatarURL.setBackground(GRAY);
        avatarURL.setForeground(NOT_QUITE_BLACK);
        avatarURL.setFont(new Font("Calibri",Font.PLAIN,20));
        avatarURL.setHorizontalAlignment(JTextField.CENTER);

        // Create the JScrollPane for the avatar URL field
        JScrollPane textScroller = new JScrollPane(avatarURL);
        textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScroller.setBorder(BorderFactory.createEmptyBorder());

        // Add text scroller to the main JPanel
        mainField.add(textScroller);

        return mainField;
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
        mainMessagePanel.setBackground(DARK_GRAY);
        mainMessagePanel.setLayout(new BoxLayout(mainMessagePanel, BoxLayout.PAGE_AXIS));

        // Create the JLabel for the field's title, and formatting
        JLabel messageTitle = new JLabel("Message");
        messageTitle.setFont(new Font("Calibri",Font.BOLD,36));
        messageTitle.setBackground(DARK_GRAY);
        messageTitle.setForeground(WHITE);
        messageTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMessagePanel.add(messageTitle);
        mainMessagePanel.add(Box.createRigidArea(new Dimension(0,5)));

        // Create the JTextArea for entering the message's contents, and formatting
        JTextArea messageBox = new JTextArea();
        messageBox.setBackground(GRAY);
        messageBox.setForeground(NOT_QUITE_BLACK);
        messageBox.setFont(new Font("Calibri",Font.PLAIN,18));
        messageBox.setLineWrap(true);
        messageBox.setWrapStyleWord(true);
        ((AbstractDocument)messageBox.getDocument()).setDocumentFilter(new LimitDocumentFilter(2000));

        // Add JScrollPane to the messageBox
        JScrollPane messageScroller = new JScrollPane(messageBox);
        messageScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageScroller.setBorder(BorderFactory.createEmptyBorder());

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
        addHoverBrightnessChange(sendMessage, .25f);

        // Add action listener for once the button is pressed
        sendMessage.addActionListener(event -> {
            // Collect username and message
            String username = usernameBox.getText().length() > 0 ? usernameBox.getText() : null;
            String message = messageBox.getText();

            // Check message length
            if(message.length() == 0)
                JOptionPane.showMessageDialog(this, "Message must be longer than 0 characters.");
            else
                new Thread(() -> {
                    try {
                        // Sets the button's color depending on if the message send was successful
                        if(DiscordAPI.sendMessage(username, avatarURL.getText().length() > 0 ? avatarURL.getText() : null, message, id, token))
                            sendMessage.setBackground(GREEN);
                        else
                            sendMessage.setBackground(RED);

                        // Schedules the color to change back
                        executor.schedule(
                                () -> sendMessage.setBackground(BLURPLE),
                                3, TimeUnit.SECONDS
                        );
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, e.getMessage());
                    }
                }).start();
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
