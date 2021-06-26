package guiComponents.guis;

import guiComponents.JPanelEssentials;
import guiComponents.RoundedBorder;
import org.jetbrains.annotations.NotNull;
import guiComponents.LimitDocumentFilter;
import other.Webhook;
import other.WebhookGUI;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class WebhookCreateConsole extends JPanelEssentials {
    private JTextField webhookNameBox;
    private File avatarFile;
    private JComboBox<String> channelIDField;
    private JTextField guildIDField;

    public WebhookCreateConsole() {
        setTitle("Webhook Creator");
        setSize(1100, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setBackground(NOT_QUITE_BLACK);

        add(frameTitle(), BorderLayout.NORTH);
        add(createPanel(), BorderLayout.CENTER);

        add(padding(NOT_QUITE_BLACK), BorderLayout.WEST);
        add(padding(NOT_QUITE_BLACK), BorderLayout.EAST);

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(NOT_QUITE_BLACK);
        bottomPanel.setLayout(new GridBagLayout());

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = .9;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 40, 20, 5);
        bottomPanel.add(createWebhook(), gbc);

        gbc.weightx = .1;
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 5, 20, 40);
        bottomPanel.add(cancel(this), gbc);
        add(bottomPanel, BorderLayout.SOUTH);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
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

        JLabel upperText = new JLabel("Webhook Creator",SwingConstants.CENTER);
        upperText.setFont(new Font("Calibri",Font.BOLD,36));
        upperText.setForeground(WHITE);

        upper.add(padding(NOT_QUITE_BLACK), BorderLayout.NORTH);
        upper.add(upperText, BorderLayout.CENTER);

        return upper;
    }

    private JPanel createPanel() {
        JPanel createPanel = new JPanel();
        createPanel.setBackground(DARK_GRAY);
        createPanel.setLayout(new GridBagLayout());

        JPanel leftCreatePanel = new JPanel();
        leftCreatePanel.setOpaque(false);
        leftCreatePanel.setLayout(new BoxLayout(leftCreatePanel, BoxLayout.PAGE_AXIS));

        // Initial GBC
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = .5;

        // Webhook Name
        JPanel webhookName = webhookName();
        leftCreatePanel.add(webhookName);

        leftCreatePanel.add(Box.createRigidArea(new Dimension(0, 100)));

        // Channel ID
        JPanel channelIDField = channelIDField();
        leftCreatePanel.add(channelIDField);

        // Right Panel
        JPanel rightCreatePanel = new JPanel();
        rightCreatePanel.setOpaque(false);
        rightCreatePanel.setLayout(new BoxLayout(rightCreatePanel, BoxLayout.PAGE_AXIS));

        // Avatar Field
        JPanel avatarField = avatarField();
        rightCreatePanel.add(avatarField, gbc);

        rightCreatePanel.add(Box.createRigidArea(new Dimension(0, 100)));

        // Guild ID Field
        JPanel guildIDField = guildIDField();
        rightCreatePanel.add(guildIDField);

        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(0,30,0,0);
        createPanel.add(leftCreatePanel, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0,30,0,30);
        createPanel.add(rightCreatePanel, gbc);

        return createPanel;
    }

    private JPanel webhookName() {
        JPanel field = new JPanel();
        field.setBackground(DARK_GRAY);
        field.setLayout(new BoxLayout(field, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel("Webhook Name");
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.add(label);
        field.add(Box.createRigidArea(new Dimension(0,5)));

        webhookNameBox = new JTextField();
        ((AbstractDocument)webhookNameBox.getDocument()).setDocumentFilter(new LimitDocumentFilter(80));
        webhookNameBox.setBackground(GRAY);
        webhookNameBox.setForeground(NOT_QUITE_BLACK);
        webhookNameBox.setFont(new Font("Calibri",Font.PLAIN,20));
        webhookNameBox.setText("Web Hooker");
        webhookNameBox.setHorizontalAlignment(JTextField.CENTER);

        JScrollPane textScroller = new JScrollPane(webhookNameBox);
        textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScroller.setBackground(DARK_GRAY);
        textScroller.setBorder(BorderFactory.createEmptyBorder());
        textScroller.setPreferredSize(new Dimension(0, 60));

        field.add(textScroller);

        return field;
    }

    private JPanel avatarField() {
        JPanel avatarPanel = new JPanel();
        avatarPanel.setOpaque(false);
        avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel("Avatar File");
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarPanel.add(label);
        avatarPanel.add(Box.createRigidArea(new Dimension(0,5)));

        JPanel subPanel = new JPanel();
        subPanel.setBackground(DARK_GRAY);
        subPanel.setLayout(new GridBagLayout());

        JTextField avatarFileName = new JTextField();
        avatarFileName.setBackground(GRAY);
        avatarFileName.setForeground(NOT_QUITE_BLACK);
        avatarFileName.setFont(new Font("Calibri",Font.PLAIN,20));
        avatarFileName.setText("No file selected...");
        avatarFileName.setEditable(false);

        JScrollPane avatarFileScroll = new JScrollPane(avatarFileName);
        avatarFileScroll.setBorder(BorderFactory.createEmptyBorder());
        avatarFileScroll.setPreferredSize(new Dimension(0, 60));

        JButton selectAvatarButton = new JButton("Find..");
        selectAvatarButton.setToolTipText("Select avatar");
        selectAvatarButton.setBackground(BLURPLE);
        selectAvatarButton.setForeground(WHITE);
        selectAvatarButton.setFont(new Font("Calibri",Font.BOLD,16));
        selectAvatarButton.setBorder(new RoundedBorder(Color.BLACK, 0, 4));
        addHoverBrightnessChange(selectAvatarButton, .25f);
        selectAvatarButton.setFocusable(false);

        // File chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setPreferredSize(new Dimension(800, 600));
        fileChooser.setDialogTitle("Select Avatar File");
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase(Locale.ROOT).endsWith(".png");
            }

            @Override
            public String getDescription() {
                return ".png";
            }
        });
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        selectAvatarButton.addActionListener(event -> {
            fileChooser.getActionMap().get("viewTypeDetails").actionPerformed(null);
            if (fileChooser.showDialog(this, "Load") == JFileChooser.APPROVE_OPTION) {
                avatarFile = fileChooser.getSelectedFile();
                avatarFileName.setText(avatarFile.getAbsolutePath());
            }
        });

        // Add elements
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .8;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        subPanel.add(avatarFileScroll, gbc);

        gbc.gridx = 1;
        gbc.weightx = .2;
        gbc.insets = new Insets(0, 10, 0, 0);
        subPanel.add(selectAvatarButton, gbc);

        avatarPanel.add(subPanel);

        return avatarPanel;
    }

    @SuppressWarnings("DuplicatedCode")
    private JPanel channelIDField() {
        JPanel field = new JPanel();
        field.setOpaque(false);
        field.setLayout(new BoxLayout(field, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel("Channel");
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.add(label);
        field.add(Box.createRigidArea(new Dimension(0,5)));

        List<String> tempList = WebhookGUI.GUI.BOT.getGuilds().get(0).getTextChannels().stream().map(channel -> channel.getName() + ":" + channel.getId()).collect(Collectors.toList());
        String[] channels = new String[tempList.size()];
        channelIDField = new JComboBox<>(tempList.toArray(channels));
        channelIDField.setBackground(GRAY);
        channelIDField.setForeground(NOT_QUITE_BLACK);
        channelIDField.setFont(new Font("Calibri",Font.PLAIN,20));
        channelIDField.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        JLabel comboLabel = ((JLabel)channelIDField.getRenderer());
        comboLabel.setHorizontalAlignment(JLabel.CENTER);

        JScrollPane textScroller = new JScrollPane(channelIDField);
        textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScroller.setBackground(GRAY);
        textScroller.setBorder(BorderFactory.createEmptyBorder());
        textScroller.setPreferredSize(new Dimension(0, 60));

        field.add(textScroller);

        return field;
    }

    @SuppressWarnings("DuplicatedCode")
    private JPanel guildIDField() {
        JPanel field = new JPanel();
        field.setOpaque(false);
        field.setLayout(new BoxLayout(field, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel("Guild ID");
        label.setFont(new Font("Calibri",Font.BOLD,36));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.add(label);
        field.add(Box.createRigidArea(new Dimension(0,5)));

        guildIDField = new JTextField();
        ((AbstractDocument) guildIDField.getDocument()).setDocumentFilter(new LimitDocumentFilter(100));
        guildIDField.setBackground(GRAY);
        guildIDField.setForeground(NOT_QUITE_BLACK);
        guildIDField.setFont(new Font("Calibri",Font.PLAIN,20));
        guildIDField.setText("1234567890");
        guildIDField.setHorizontalAlignment(JTextField.CENTER);

        JScrollPane textScroller = new JScrollPane(guildIDField);
        textScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScroller.setBackground(GRAY);
        textScroller.setBorder(BorderFactory.createEmptyBorder());
        textScroller.setPreferredSize(new Dimension(0, 60));

        field.add(textScroller);

        return field;
    }

    private JPanel createWebhook() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(DARK_GRAY);
        buttonPanel.setLayout(new GridBagLayout());

        JButton createWebhook = new JButton("Finalize Webhook");
        createWebhook.setBackground(BLURPLE);
        createWebhook.setForeground(WHITE);
        createWebhook.setFont(new Font("Calibri", Font.BOLD, 40));
        createWebhook.setBorder(new RoundedBorder(MID_GRAY, 0, 16));
        createWebhook.setFocusable(false);
        addHoverBrightnessChange(createWebhook, .25f);

        createWebhook.addActionListener(event -> {
            String username = webhookNameBox.getText();
            String channelID = String.valueOf(channelIDField.getSelectedItem()).split(":")[1];
            String guildID = guildIDField.getText();
            if (username.length() == 0)
                JOptionPane.showMessageDialog(this, "Username must be longer than 0 characters.");
            else if (channelID.length() == 0)
                JOptionPane.showMessageDialog(this, "Must specify channel.");
            else if (avatarFile == null)
                JOptionPane.showMessageDialog(this, "Must have avatar file.");
            else {
                if (Webhook.createWebhook(username, avatarFile, channelID)) {
                    WebhookGUI.GUI.MAIN_CONSOLE.setEnabled(true);
                    this.dispose();
                }
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        buttonPanel.add(createWebhook, gbc);

        return buttonPanel;
    }
}
