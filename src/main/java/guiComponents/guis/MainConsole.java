package guiComponents.guis;

import guiComponents.JFrameEssentials;
import guiComponents.RoundedBorder;
import org.jetbrains.annotations.NotNull;
import other.Webhook;
import other.WebhookGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main console of the Webhook Manager. This contains the list of {@link Webhook}s present in the {@link net.dv8tion.jda.api.entities.Guild}s
 * the assigned bot is in. From here you can create {@link Webhook}s among other functions
 */
public class MainConsole extends JFrameEssentials {
    /**
     * The JPanel for housing the list of {@link Webhook}s. Updated by {@link MainConsole#populateList()}
     */
    public JPanel webhookList;

    /**
     * The basic constructor for building the Main Console
     */
    public MainConsole() {
        // Setup basic console
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

        // Update UI
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
        // Create the JPanel for housing the JLabel
        JPanel upper = new JPanel();
        upper.setLayout(new BorderLayout());
        upper.setBackground(NOT_QUITE_BLACK);

        // Create the title JLabel
        JLabel upperText = new JLabel("Webhook Viewer",SwingConstants.CENTER);
        upperText.setFont(new Font("Calibri",Font.BOLD,36));
        upperText.setForeground(WHITE);

        // Add upperText to upper with formatting
        upper.add(padding(NOT_QUITE_BLACK), BorderLayout.NORTH);
        upper.add(upperText, BorderLayout.CENTER);

        return upper;
    }

    /**
     * The {@link JPanel} containing the list of all {@link Webhook}s found within the selected {@link net.dv8tion.jda.api.entities.Guild}.
     * This list is populated by {@link MainConsole#populateList()}
     *
     * @return A {@link JPanel} containing a list of all {@link Webhook}s
     */
    private JPanel webhookList() {
        // Create JFrame for housing the webhook list
        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BorderLayout());

        // Initialize the list JPanel and formatting
        webhookList = new JPanel();
        webhookList.setLayout(new GridBagLayout());
        webhookList.setBackground(MID_GRAY);

        // House the list panel inside a JScrollPane
        JScrollPane listScroll = new JScrollPane(webhookList);
        listScroll.setBorder(BorderFactory.createEmptyBorder());

        // Populate the panel with Webhooks
        populateList();

        // Add listScroll to mainFrame
        mainFrame.add(listScroll, BorderLayout.CENTER);
        return mainFrame;
    }

    /**
     * The {@link JPanel} containing several buttons: {@link MainConsole#createButton()}, {@link MainConsole#refreshButton()}, and {@link MainConsole#settingsButton()}
     *
     * @return A {@link JPanel} containing several buttons
     */
    private JPanel createPanel() {
        // Initialize and format the JPanel for all the buttons
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new GridBagLayout());
        addButtonPanel.setBackground(NOT_QUITE_BLACK);
        addButtonPanel.setBorder(BorderFactory.createEmptyBorder());

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
        addHoverBrightnessChange(addButton, .25f);
        addButton.setBorder(new RoundedBorder(NOT_QUITE_BLACK,0,16));

        // Add action listener for creating the new webhooks once clicking the button
        addButton.addActionListener(event -> {
            new WebhookCreateConsole();
            setEnabled(false);
        });

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
        addHoverBrightnessChange(refresh, .25f);
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
            populateList()
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
        addHoverBrightnessChange(settings, .25f);
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
        settings.addActionListener(action -> {

        });

        return settings;
    }

    /**
     * Takes a {@link JPanel} and fills it with the list of all {@link Webhook}s in the selected {@link net.dv8tion.jda.api.entities.Guild}.
     * This method is asynchronous.
     */
    public void populateList() {
        // Attempt to fetch all the webhooks in the guild
        // TODO Proper errors and multiple Guild support
        WebhookGUI.GUI.BOT.getGuilds().get(0).retrieveWebhooks().queue(rawWebhooks -> {
            // Create the list of Webhooks
            List<Webhook> webhooks = rawWebhooks.stream().map(hook -> new Webhook(hook.getName(), hook.getToken(), hook.getId(), hook.getChannel().getId())).collect(Collectors.toList());

            // Create GBC for formatting
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.weighty = 1;

            // Remove all components from the JPanel
            webhookList.removeAll();

            // Add filler JPanel
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            webhookList.add(filler, gbc);

            // Update GBC constraints
            gbc.insets = new Insets(10, 10, 0, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weighty = GridBagConstraints.RELATIVE;

            // Go through the list of webhooks and add them to the JPanel
            if(webhooks.size() != 0) {
                for (int i = webhooks.size() - 1; i >= 0; i--) {
                    JPanel webhookPanel = webhooks.get(i).createPanel();
                    webhookList.add(webhookPanel, gbc, 0);
                    Dimension tempDimension = webhookPanel.getPreferredSize();
                    tempDimension.height = 100;
                    webhookPanel.setPreferredSize(tempDimension);
                }
            }

            // Refresh the console to display update webhook list
            validate();
            repaint();
        });
    }
}
