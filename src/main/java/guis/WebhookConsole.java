package guis;

import org.jetbrains.annotations.NotNull;
import other.LimitDocumentFilter;
import other.Webhook;
import other.WebhookGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class WebhookConsole extends JPanelEssentials {
    private JTextField usernameBox;
    private JTextField avatarURL;
    private final String id;
    private final String token;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public WebhookConsole(String id, String token) {
        this.id = id;
        this.token = token;

        setTitle("Webhook Message Console");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setBackground(NOT_QUITE_BLACK);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        add(frameTitle(), BorderLayout.NORTH);
        add(userInputPanel(), BorderLayout.CENTER);

        add(padding(NOT_QUITE_BLACK), BorderLayout.WEST);
        add(padding(NOT_QUITE_BLACK), BorderLayout.EAST);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                WebhookGUI.GUI.BOT.getGuilds().get(0).retrieveWebhooks().queue(webhooks ->
                        WebhookGUI.GUI.MAIN_CONSOLE.populateList(WebhookGUI.GUI.MAIN_CONSOLE.list, webhooks.stream().map(hook -> new Webhook(hook.getName(), hook.getToken(), hook.getId(), hook.getChannel().getId())).collect(Collectors.toList()))
                );
                WebhookGUI.GUI.MAIN_CONSOLE.setEnabled(true);
            }
        });

        setVisible(true);
    }

    @NotNull
    private JPanel frameTitle() {
        JPanel upper = new JPanel();
        upper.setLayout(new BorderLayout());
        upper.setBackground(NOT_QUITE_BLACK);

        JLabel upperText = new JLabel("Webhook Message Sender",SwingConstants.CENTER);
        upperText.setFont(new Font("Calibri",Font.BOLD,36));
        upperText.setForeground(WHITE);

        upper.add(padding(NOT_QUITE_BLACK), BorderLayout.NORTH);
        upper.add(upperText, BorderLayout.CENTER);

        return upper;
    }

    private JPanel userInputPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(DARK_GRAY);
        panel.setLayout(new GridBagLayout());

        JPanel subPanelTop = new JPanel();
        subPanelTop.setBackground(DARK_GRAY);
        subPanelTop.setLayout(new GridBagLayout());

        GridBagConstraints gbcSub = new GridBagConstraints();
        gbcSub.gridx = 0;
        gbcSub.gridy = 0;
        gbcSub.weightx = .75;
        gbcSub.weighty = 1;
        gbcSub.fill = GridBagConstraints.BOTH;
        gbcSub.insets = new Insets(40, 40, 40, 20);

        subPanelTop.add(usernameField(), gbcSub);

        gbcSub.gridx = 1;
        gbcSub.weightx = .25;
        gbcSub.insets = new Insets(40, 20, 40, 40);

        subPanelTop.add(avatarURLField(), gbcSub);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = .1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(subPanelTop, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = .9;
        gbc.weightx = 1;
        gbc.insets = new Insets(0,40,40,40);
        panel.add(message(), gbc);

        return panel;
    }

    @NotNull
    private JPanel usernameField() {
        JPanel field = new JPanel();
        field.setBackground(DARK_GRAY);
        field.setLayout(new BoxLayout(field, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel("Username");
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.add(label);
        field.add(Box.createRigidArea(new Dimension(0,5)));

        usernameBox = new JTextField();
        ((AbstractDocument)usernameBox.getDocument()).setDocumentFilter(new LimitDocumentFilter(80));
        usernameBox.setBackground(GRAY);
        usernameBox.setForeground(NOT_QUITE_BLACK);
        usernameBox.setFont(new Font("Calibri",Font.PLAIN,20));
        usernameBox.setHorizontalAlignment(JTextField.CENTER);

        JScrollPane textScroller = new JScrollPane(usernameBox);
        textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScroller.setBackground(GRAY);
        textScroller.setBorder(BorderFactory.createEmptyBorder());

        field.add(textScroller);

        return field;
    }

    @NotNull
    private JPanel avatarURLField() {
        JPanel mainField = new JPanel();
        mainField.setBackground(DARK_GRAY);
        mainField.setLayout(new BoxLayout(mainField, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel("Avatar URL");
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainField.add(label);
        mainField.add(Box.createRigidArea(new Dimension(0,5)));

        avatarURL = new JTextField();
        ((AbstractDocument)avatarURL.getDocument()).setDocumentFilter(new LimitDocumentFilter(80));
        avatarURL.setBackground(GRAY);
        avatarURL.setForeground(NOT_QUITE_BLACK);
        avatarURL.setFont(new Font("Calibri",Font.PLAIN,20));
        avatarURL.setHorizontalAlignment(JTextField.CENTER);

        JScrollPane textScroller = new JScrollPane(avatarURL);
        textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScroller.setBorder(BorderFactory.createEmptyBorder());

        mainField.add(textScroller);

        return mainField;
    }

    private JPanel message() {
        JPanel mainMessagePanel = new JPanel();
        mainMessagePanel.setBackground(DARK_GRAY);
        mainMessagePanel.setLayout(new BoxLayout(mainMessagePanel, BoxLayout.PAGE_AXIS));

        JLabel messageTitle = new JLabel("Message");
        messageTitle.setFont(new Font("Calibri",Font.BOLD,36));
        messageTitle.setBackground(DARK_GRAY);
        messageTitle.setForeground(WHITE);
        messageTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMessagePanel.add(messageTitle);
        mainMessagePanel.add(Box.createRigidArea(new Dimension(0,5)));

        JTextArea messageBox = new JTextArea();
        messageBox.setBackground(GRAY);
        messageBox.setForeground(NOT_QUITE_BLACK);
        messageBox.setFont(new Font("Calibri",Font.PLAIN,18));
        messageBox.setLineWrap(true);
        messageBox.setWrapStyleWord(true);
        ((AbstractDocument)messageBox.getDocument()).setDocumentFilter(new LimitDocumentFilter(2000));

        JScrollPane messageScroller = new JScrollPane(messageBox);
        messageScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageScroller.setBorder(BorderFactory.createEmptyBorder());

        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(DARK_GRAY);
        messagePanel.setLayout(new GridBagLayout());

        JButton sendMessage = new JButton("Send Message");
        sendMessage.setBackground(BLURPLE);
        sendMessage.setForeground(WHITE);
        sendMessage.setFont(new Font("Calibri",Font.BOLD,40));
        sendMessage.setBorder(new RoundedBorder(Color.BLACK, 0, 16, 0));
        sendMessage.setFocusable(false);
        addHoverBrightnessChange(sendMessage, .25f);

        sendMessage.addActionListener(event -> {
            String username = usernameBox.getText().length() > 0 ? usernameBox.getText() : null;
            String message = messageBox.getText();

            if(message.length() == 0)
                JOptionPane.showMessageDialog(this, "Message must be longer than 0 characters.");
            else
                new Thread(() -> {
                    try {
                        if(WebhookGUI.sendMessage(username, avatarURL.getText().length() > 0 ? avatarURL.getText() : null, message, id, token))
                            sendMessage.setBackground(GREEN);
                        else
                            sendMessage.setBackground(RED);

                        executor.schedule(
                                () -> sendMessage.setBackground(BLURPLE),
                                3, TimeUnit.SECONDS
                        );
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, e.getMessage());
                    }
                }).start();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        messagePanel.add(messageScroller, gbc);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(NOT_QUITE_BLACK);

        gbc.weighty = 1;
        gbc.weightx = .9;
        gbc.insets = new Insets(20,50, 20, 5);
        buttonPanel.add(sendMessage, gbc);

        gbc.weightx = .1;
        gbc.insets = new Insets(20,5, 20, 50);
        gbc.gridx = 1;
        buttonPanel.add(cancel(this), gbc);
        add(buttonPanel, BorderLayout.SOUTH);

        mainMessagePanel.add(messagePanel);

        return mainMessagePanel;
    }
}
