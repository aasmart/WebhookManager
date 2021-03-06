package com.smart.manager.guiComponents.guis;

import com.smart.manager.guiComponents.JFrameEssentials;
import com.smart.manager.guiComponents.RoundedBorder;
import com.smart.manager.guiComponents.popups.InvalidPermsPopUp;
import com.smart.manager.guiComponents.popups.NoGuildsPopUp;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import com.smart.manager.Webhook;
import com.smart.manager.WebhookGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main console of the Webhook Manager. This contains the list of {@link Webhook}s present in the {@link net.dv8tion.jda.api.entities.Guild}s
 * the assigned bot is in. From here you can create {@link Webhook}s among other functions
 */
public class MainConsole extends JFrameEssentials {
    /**
     * A {@link JTabbedPane} with tabs linking to all the guids the given bot is in with their list of webhooks
     */
    public JTabbedPane guildPanel;

    /**
     * Maps a tab index to a guild ID
     */
    public LinkedHashMap<Integer, Long> tabGuildIDMap = new LinkedHashMap<>();

    /**
     * Maps a guild ID to a {@link JPanel} with a list of {@link Webhook}
     */
    public LinkedHashMap<Long, JPanel> webhookListPanels = new LinkedHashMap<>();

    /**
     * The basic constructor for building the Main Console
     */
    public MainConsole() {
        WebhookGUI.GUI.MAIN_CONSOLE = this;

        // Setup basic console
        setTitle("Webhook Manager");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Update UI
        UIManager.put("OptionPane.background", NOT_QUITE_BLACK);
        UIManager.put("Panel.background", NOT_QUITE_BLACK);
        UIManager.put("OptionPane.messageForeground", WHITE);
        UIManager.put("TabbedPane.foreground", WHITE);
        UIManager.put("TabbedPane.opaque", true);
        UIManager.put("TabbedPane.selected", MID_GRAY);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.light", NOT_QUITE_BLACK);
        UIManager.put("TabbedPane.highlight", NOT_QUITE_BLACK);
        UIManager.put("TabbedPane.shadow", NOT_QUITE_BLACK);
        UIManager.put("TabbedPane.darkShadow", NOT_QUITE_BLACK);
        UIManager.put("TextField.caretForeground", WHITE);
        UIManager.put("TextField.caretBackground", WHITE);

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        updateMainConsole();
    }

    /**
     * Updates the GUI's Main display panel
     */
    public void updateMainConsole() {
        MainConsole mainConsole = WebhookGUI.GUI.MAIN_CONSOLE;
        mainConsole.getContentPane().removeAll();

        mainConsole.getContentPane().setLayout(new BorderLayout());
        mainConsole.getContentPane().setBackground(NOT_QUITE_BLACK);

        // Panel for the webhook list and such
        JPanel mainConsolePanel = new JPanel(new BorderLayout());
        mainConsolePanel.setOpaque(false);

        // Padding
        mainConsolePanel.add(padding(NOT_QUITE_BLACK), BorderLayout.WEST);
        mainConsolePanel.add(padding(NOT_QUITE_BLACK), BorderLayout.EAST);

        // Add various panels
        mainConsolePanel.add(frameTitle(), BorderLayout.NORTH);
        mainConsolePanel.add(createButtonsPanel(), BorderLayout.SOUTH);

        // Create JPanel for the center
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(null);

        // GBC for center formatting
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .4;
        gbc.weighty = 1;

        // Add webhook list
        centerPanel.add(createGuildPanel(), gbc);
        if(!webhookList()) {
            WebhookGUI.GUI.MAIN_CONSOLE = null;
            return;
        }

        // Add settings panel
        gbc.weightx = .6;
        JPanel settingPanel = new JPanel();
        SettingsConsole.buildSettingsConsole(settingPanel);
        centerPanel.add(settingPanel, gbc);

        // Add various panels
        mainConsolePanel.add(centerPanel, BorderLayout.CENTER);
        mainConsole.getContentPane().add(mainConsolePanel);

        mainConsole.validate();
        mainConsole.repaint();
    }

    /**
     * The title of the GUI
     * @return A {@link JPanel} with the title
     */
    @NotNull
    private JPanel frameTitle() {
        // Create the JPanel for housing the JLabel
        JPanel upper = new JPanel(new GridBagLayout());
        upper.setBackground(NOT_QUITE_BLACK);

        // GBC for formatting
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = .4;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 0, 0, 0);

        // Create the title JLabel
        JLabel upperText = new JLabel("Webhook Viewer",SwingConstants.CENTER);
        upperText.setFont(new Font("Calibri",Font.BOLD,36));
        upperText.setForeground(WHITE);

        // Add upperText to upper with formatting
        upper.add(upperText, gbc);

        // Add Settings title
        gbc.weightx = .6;
        gbc.insets = new Insets(20, 0, 0, 0);
        upper.add(SettingsConsole.frameTitle(), gbc);

        // Add padding so the components are centered properly
        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.add(padding(NOT_QUITE_BLACK), BorderLayout.WEST);
        paddingPanel.add(padding(NOT_QUITE_BLACK), BorderLayout.EAST);
        paddingPanel.add(upper, BorderLayout.CENTER);
        return paddingPanel;
    }

    /**
     * Creates the {@link JTabbedPane} containing all the {@link Guild Guilds} and {@link Webhook Webhooks}
     * @return A {@link JTabbedPane}
     */
    private JTabbedPane createGuildPanel() {
        guildPanel = new JTabbedPane();
        guildPanel.setBorder(BorderFactory.createEmptyBorder());
        guildPanel.setFocusable(false);
        guildPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        guildPanel.setBackground(NOT_QUITE_BLACK);

        return guildPanel;
    }

    /**
     * Initializes {@link MainConsole#guildPanel} containing the list of all {@link Webhook}s found within the selected
     * {@link net.dv8tion.jda.api.entities.Guild}. This list is populated by {@link MainConsole#populateList(long)}
     */
    public boolean webhookList() {
        // Get the list of guilds
        List<Guild> guilds = WebhookGUI.GUI.BOT.getGuilds();
        guildPanel.removeAll();

        // Iterate through the guilds and create
        for(int i = 0; i < guilds.size(); i++) {
            Guild guild = guilds.get(i);
            // Initialize the list JPanel and formatting
            JPanel webhookList = new JPanel();
            webhookList.setLayout(new GridBagLayout());
            webhookList.setBackground(MID_GRAY);

            // House the list panel inside a JScrollPane
            JScrollPane listScroll = new JScrollPane(webhookList);
            listScroll.setBorder(null);
            listScroll.setBackground(NOT_QUITE_BLACK);
            listScroll.setPreferredSize(new Dimension(0, 0)); // Don't ask... just don't

            // Update Scrollbar styles
            standardizeScrollbar(listScroll.getHorizontalScrollBar());
            standardizeScrollbar(listScroll.getVerticalScrollBar());

            // Populate the panel with Webhooks
            webhookListPanels.put(guild.getIdLong(), webhookList);

            // Create tab and formatting
            guildPanel.addTab(guild.getName(), listScroll);
            guildPanel.setBackgroundAt(i, BLURPLE);
            if(!populateList(guild.getIdLong()))
                return false;

            // Add tab index to tabGuildIDMap
            tabGuildIDMap.put(i, guild.getIdLong());
        }
        return true;
    }

    /**
     * The {@link JPanel} containing several buttons: {@link MainConsole#createButton()}, {@link MainConsole#refreshButton()}, and {@link MainConsole#settingsButton()}
     *
     * @return A {@link JPanel} containing several buttons
     */
    private JPanel createButtonsPanel() {
        // Initialize and format the JPanel for all the buttons
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new GridBagLayout());
        addButtonPanel.setBackground(NOT_QUITE_BLACK);
        addButtonPanel.setBorder(BorderFactory.createLineBorder(NOT_QUITE_BLACK, 1));

        // Get the "Add Button"
        JButton addButton = createButton();

        // Create GBC for formatting
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = .9;
        gbc.insets = new Insets(10, 10, 10, 0);

        // Add the "Add Button"
        addButtonPanel.add(addButton, gbc);

        // Formatting and adding the "Refresh Button"
        gbc.weightx = .05;
        gbc.insets = new Insets(10, 10, 10, 0);
        addButtonPanel.add(refreshButton(), gbc);

        // Formatting and adding the "Settings Button"
        gbc.insets = new Insets(10, 10, 10, 10);
        addButtonPanel.add(settingsButton(), gbc);

        // Update dimensions of the "Add Button"
        Dimension tempButtonDimension = addButton.getPreferredSize();
        tempButtonDimension.height = 100;
        addButton.setPreferredSize(tempButtonDimension);

        return addButtonPanel;
    }

    /**
     * Create a {@link JButton} for creating new {@link Webhook}s
     *
     * @return A {@link JButton}
     */
    private JButton createButton() {
        // Create and format the button
        JButton addButton = new JButton("Create New Webhook");
        addButton.setFont(new Font("Calibri",Font.BOLD,36));
        addButton.setBackground(BLURPLE);
        addButton.setForeground(WHITE);
        addButton.setFocusable(false);
        setHoverBrightnessChange(addButton, .25f);
        addButton.setBorder(new RoundedBorder(NOT_QUITE_BLACK,0,16));

        // Add action listener for creating the new webhooks once clicking the button
        addButton.addActionListener(event -> new WebhookCreateConsole(tabGuildIDMap.get(guildPanel.getSelectedIndex())));

        return addButton;
    }

    /**
     * Creates a {@link JButton} for refreshing the list of {@link Webhook}s
     *
     * @return A {@link JButton}
     */
    private JButton refreshButton() {
        // Create JButton and format
        JButton refresh = new JButton();
        refresh.setBackground(BLURPLE);
        refresh.setForeground(WHITE);
        refresh.setFocusable(false);
        setHoverBrightnessChange(refresh, .25f);
        refresh.setBorder(new RoundedBorder(NOT_QUITE_BLACK,0,16));

        // Load icon
        try {
            URL resource = getClass().getResource("/refresh.png");
            if(resource == null)
                throw new NullPointerException("Could not get resource: refresh.png");
            Image img = ImageIO.read(resource);
            refresh.setIcon(new ImageIcon(img.getScaledInstance( 68, 68,  java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add action listener for refreshing the Webhooks on press
        refresh.addActionListener(action ->
            webhookList()
        );

        return refresh;
    }

    /**
     * Creates a {@link JButton} for opening the settings panel
     *
     * @return A {@link JButton}
     */
    private JButton settingsButton() {
        // Create base button and formatting
        JButton settings = new JButton();
        settings.setBackground(BLURPLE);
        settings.setForeground(WHITE);
        settings.setFocusable(false);
        setHoverBrightnessChange(settings, .25f);
        settings.setBorder(new RoundedBorder(NOT_QUITE_BLACK,0,16));

        // Load icon
        try {
            URL resource = getClass().getResource("/settings_icon.png");
            if(resource == null)
                throw new NullPointerException("Could not get resource: settings_icon.png");
            Image img = ImageIO.read(resource);
            settings.setIcon(new ImageIcon(img.getScaledInstance( 68, 68,  java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add action listener for loading the settings panel
        settings.addActionListener(action ->
            SettingsConsole.toggleSettings()
        );

        return settings;
    }

    /**
     * Takes a {@link JPanel} and fills it with the list of all {@link Webhook}s in the selected {@link net.dv8tion.jda.api.entities.Guild}.
     * This method is asynchronous.
     */
    public boolean populateList(long guildID) {
        // Attempt to fetch all the webhooks in the guild
        Guild g = WebhookGUI.GUI.BOT.getGuildById(guildID);
        // TODO Proper error
        if(g == null) {
            if(WebhookGUI.GUI.BOT.getGuilds().size() == 0) {
                WebhookGUI.GUI.MAIN_CONSOLE.dispose();
                new NoGuildsPopUp();
                return false;
            }
            return true;
        }
        try {
            g.retrieveWebhooks().queue(rawWebhooks -> {
                // Create the list of Webhooks
                List<Webhook> webhooks = rawWebhooks.stream().map(hook -> new Webhook(hook.getName(), hook.getToken(), hook.getId(), hook.getChannel().getId(), hook.getGuild())).collect(Collectors.toList());

                // Create GBC for formatting
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.weightx = 1;
                gbc.weighty = 1;

                // Remove all components from the JPanel
                webhookListPanels.get(guildID).removeAll();

                // Add filler JPanel
                JPanel filler = new JPanel();
                filler.setOpaque(false);
                webhookListPanels.get(guildID).add(filler, gbc);

                // Update GBC constraints
                gbc.insets = new Insets(10, 10, 0, 10);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weighty = GridBagConstraints.RELATIVE;

                // Go through the list of webhooks and add them to the JPanel
                if (webhooks.size() != 0) {
                    for (int i = webhooks.size() - 1; i >= 0; i--) {
                        JPanel webhookPanel = webhooks.get(i).createPanel();
                        webhookListPanels.get(guildID).add(webhookPanel, gbc, 0);
                        Dimension tempDimension = webhookPanel.getPreferredSize();
                        tempDimension.height = 100;
                        webhookPanel.setPreferredSize(tempDimension);
                    }
                }

              getContentPane().validate();
              getContentPane().repaint();
            }, fail ->
                System.out.println(fail.getMessage())
            );
        } catch (Exception e) {
            new InvalidPermsPopUp(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * An overridden function for nulling out the disposed main console
     */
    @Override
    public void dispose() {
        super.dispose();
        WebhookGUI.GUI.MAIN_CONSOLE = null;
    }
}
