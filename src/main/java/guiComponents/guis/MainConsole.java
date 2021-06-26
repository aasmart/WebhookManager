package guiComponents.guis;

import guiComponents.JPanelEssentials;
import guiComponents.RoundedBorder;
import org.jetbrains.annotations.NotNull;
import other.Webhook;
import other.WebhookGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class MainConsole extends JPanelEssentials {
    public JPanel list;
    public MainConsole() {
        setTitle("Webhook Viewer");
        setSize(600, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(NOT_QUITE_BLACK);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Padding
        add(padding(NOT_QUITE_BLACK), BorderLayout.WEST);
        add(padding(NOT_QUITE_BLACK), BorderLayout.EAST);

        // Add various panels
        add(frameTitle(), BorderLayout.NORTH);
        add(createPanel(), BorderLayout.SOUTH);
        add(webhookList(), BorderLayout.CENTER);

        UIManager.put("OptionPane.background", NOT_QUITE_BLACK);
        UIManager.put("Panel.background", NOT_QUITE_BLACK);
        UIManager.put("OptionPane.messageForeground", WHITE);

        setVisible(true);
    }

    /**
     * The title of the GUI
     * @return A {@link JPanel} with the title
     */
    @NotNull
    private JPanel frameTitle() {
        JPanel upper = new JPanel();
        upper.setLayout(new BorderLayout());
        upper.setBackground(NOT_QUITE_BLACK);

        JLabel upperText = new JLabel("Webhook Viewer",SwingConstants.CENTER);
        upperText.setFont(new Font("Calibri",Font.BOLD,36));
        upperText.setForeground(WHITE);

        upper.add(padding(NOT_QUITE_BLACK), BorderLayout.NORTH);
        upper.add(upperText, BorderLayout.CENTER);

        return upper;
    }

    private JPanel webhookList() {
        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BorderLayout());

        list = new JPanel();
        list.setLayout(new GridBagLayout());
        list.setBackground(MID_GRAY);

        JScrollPane listScroll = new JScrollPane(list);
        listScroll.setBorder(BorderFactory.createEmptyBorder());
        // TODO Make guild independent
        WebhookGUI.GUI.BOT.getGuilds().get(0).retrieveWebhooks().queue(webhooks -> {
            if (webhooks.size() != 0)
                WebhookGUI.GUI.MAIN_CONSOLE.populateList(WebhookGUI.GUI.MAIN_CONSOLE.list, webhooks.stream().map(hook -> new Webhook(hook.getName(), hook.getToken(), hook.getId(), hook.getChannel().getId())).collect(Collectors.toList()));
            }
        );

        mainFrame.add(listScroll, BorderLayout.CENTER);
        return mainFrame;
    }

    private JPanel createPanel() {
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new GridBagLayout());
        addButtonPanel.setBackground(NOT_QUITE_BLACK);
        addButtonPanel.setBorder(BorderFactory.createEmptyBorder());

        JButton addButton = new JButton("Create New Webhook");
        addButton.setFont(new Font("Calibri",Font.BOLD,36));
        addButton.setBackground(BLURPLE);
        addButton.setForeground(WHITE);
        addButton.setFocusable(false);
        addHoverBrightnessChange(addButton, .25f);

        AbstractBorder roundedBorder = new RoundedBorder(NOT_QUITE_BLACK,0,16);
        addButton.setBorder(roundedBorder);

        addButton.addActionListener(event -> {
            MainConsole mainConsole = WebhookGUI.GUI.MAIN_CONSOLE;
            mainConsole.setEnabled(false);
            new WebhookCreateConsole();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = .9;
        gbc.insets = new Insets(10, 10, 10, 0);
        addButtonPanel.add(addButton, gbc);

        gbc.weightx = .05;
        gbc.insets = new Insets(10, 10, 10, 0);
        addButtonPanel.add(refreshButton(), gbc);

        gbc.insets = new Insets(10, 10, 10, 10);
        addButtonPanel.add(settingsButton(), gbc);

        Dimension tempButtonDimension = addButton.getPreferredSize();
        tempButtonDimension.height = 100;
        addButton.setPreferredSize(tempButtonDimension);

        return addButtonPanel;
    }

    private JButton refreshButton() {
        JButton refresh = new JButton();

        refresh.setBackground(BLURPLE);
        refresh.setForeground(WHITE);
        refresh.setFocusable(false);
        addHoverBrightnessChange(refresh, .25f);

        try {
            URL resource = getClass().getResource("/refresh.png");
            if(resource == null)
                throw new NullPointerException("Could not get resource: refresh.png");
            Image img = ImageIO.read(resource);
            refresh.setIcon(new ImageIcon(img.getScaledInstance( 68, 68,  java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AbstractBorder roundedBorder = new RoundedBorder(NOT_QUITE_BLACK,0,16);
        refresh.setBorder(roundedBorder);

        refresh.addActionListener(action ->
            WebhookGUI.GUI.BOT.getGuilds().get(0).retrieveWebhooks().queue(webhooks -> {
                        if (webhooks.size() != 0)
                            WebhookGUI.GUI.MAIN_CONSOLE.populateList(WebhookGUI.GUI.MAIN_CONSOLE.list, webhooks.stream().map(hook -> new Webhook(hook.getName(), hook.getToken(), hook.getId(), hook.getChannel().getId())).collect(Collectors.toList()));
                    }
            )
        );

        return refresh;
    }

    private JButton settingsButton() {
        JButton refresh = new JButton();

        refresh.setBackground(BLURPLE);
        refresh.setForeground(WHITE);
        refresh.setFocusable(false);
        addHoverBrightnessChange(refresh, .25f);

        try {
            URL resource = getClass().getResource("/settings_icon.png");
            if(resource == null)
                throw new NullPointerException("Could not get resource: settings_icon.png");
            Image img = ImageIO.read(resource);
            refresh.setIcon(new ImageIcon(img.getScaledInstance( 68, 68,  java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AbstractBorder roundedBorder = new RoundedBorder(NOT_QUITE_BLACK,0,16);
        refresh.setBorder(roundedBorder);

        refresh.addActionListener(action -> {

        });

        return refresh;
    }

    public void populateList(JPanel panel, List<Webhook> webhooks) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        panel.removeAll();

        JPanel filler = new JPanel();
        filler.setOpaque(false);
        panel.add(filler, gbc);

        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = GridBagConstraints.RELATIVE;

        if(webhooks.size() != 0) {
            for (int i = webhooks.size() - 1; i >= 0; i--) {
                JPanel webhookPanel = webhooks.get(i).createPanel();
                panel.add(webhookPanel, gbc, 0);
                Dimension tempDimension = webhookPanel.getPreferredSize();
                tempDimension.height = 100;
                webhookPanel.setPreferredSize(tempDimension);
            }
        }

        validate();
        repaint();
    }
}
