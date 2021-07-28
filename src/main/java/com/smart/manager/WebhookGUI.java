package com.smart.manager;

import com.smart.manager.events.JoinGuild;
import com.smart.manager.events.LeaveGuild;
import com.smart.manager.events.Startup;
import com.smart.manager.guiComponents.guis.MainConsole;
import com.smart.manager.guiComponents.popups.InvalidPermsPopUp;
import com.smart.manager.guiComponents.popups.NoGuildsPopUp;
import com.smart.manager.guiComponents.popups.TokenPopUp;
import com.smart.manager.guiComponents.settings.ManagerSettings;
import com.smart.manager.guiComponents.settings.Setting;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * The main class for the Webhook Manager, housing the bot and the {@link MainConsole}
 */
public class WebhookGUI {
    /**
     * The bot assigned to the WebhookGUI
     */
    public JDA BOT;

    /**
     * The WebhookGUI's {@link MainConsole}
     */
    public MainConsole MAIN_CONSOLE;

    /**
     * The main WebhookGUI object
     */
    public static WebhookGUI GUI = new WebhookGUI();

    /**
     * A list containing all the {@link Setting}s for the Webhook Manager. Created by {@link ManagerSettings}
     */
    public static List<Setting> settings;

    /**
     * The manager's associated {@link Properties} file
     */
    public static Properties managerProperties;

    /**
     * The constructor for setting up the WebhookGUI
     */
    public WebhookGUI() {
        // Read bot token
        String token = readToken();
        if(token == null || token.length() == 0)
            return;

        new InvalidPermsPopUp("EEE");
        managerProperties = readProperties();

        try {
            JDABuilder builder = JDABuilder.createDefault(token)
                    .addEventListeners(
                            new Startup(),
                            new JoinGuild(),
                            new LeaveGuild()
                    )
                    .setRawEventsEnabled(true)
                    .setActivity(makeActivity(managerProperties.getProperty("activity-type"), managerProperties.getProperty("activity-desc")));

            BOT = builder.build();
        } catch (LoginException e) {
            new TokenPopUp("Invalid Token!");
        }
    }

    public static void main(String[] args) { }

    /**
     * Reads the bot token from the save file
     *
     * @return A {@link String} containing the bot token
     */
    public static String readToken() {
        // Get token file location and instantiate other variables
        String tokenLocation = System.getProperty("user.home") + "\\WebhookManager\\bot_token.secret";
        BufferedReader reader = null;
        String token = "";

        // Attempt to read token
        try {
            reader = new BufferedReader(new FileReader(tokenLocation));
            token = reader.readLine();

            if(token == null || token.length() == 0)
                new TokenPopUp("No Token Found!");
        } catch (IOException e) {
            new TokenPopUp("No Token Found!");
        } finally {
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return token;
    }

    /**
     * Saves the bot token to the save file
     *
     * @param token The token to save
     * @return If writing the token was successful
     */
    @SuppressWarnings("unused")
    public static boolean writeToken(String token) {
        // Create token location
        String tokenLocation = System.getProperty("user.home") + "\\WebhookManager\\bot_token.secret";
        File f = new File(tokenLocation);

        // Attempt to write token
        BufferedWriter writer = null;
        try {
            if(!f.isFile()) {
                final boolean mkdir = f.getParentFile().mkdir();
                final boolean created = f.createNewFile();
            }

            writer = new BufferedWriter(new FileWriter(f));
            writer.write(token);
        } catch (IOException e) {
            e.printStackTrace();
            new TokenPopUp("Couldn't Write Token!");
            return false;
        } finally {
            try {
                if(writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Reads the {@link Properties} file associated with the manager
     * @return A {@link Properties} file
     */
    @SuppressWarnings("unused")
    public static Properties readProperties() {
        // Retrieve the file location
        String propertiesLocation = System.getProperty("user.home") + "\\WebhookManager\\manager_props.properties";
        File f = new File(propertiesLocation);

        // Create Properties File object
        Properties p = new Properties();

        // Setup a FileInputStream
        FileInputStream inputStream = null;
        try {
            // Create the properties file if it doesn't exist
            if(!f.isFile()) {
                final boolean mkdir = f.getParentFile().mkdir();
                final boolean created = f.createNewFile();
                return getDefaultProperties();
            } else {
                // Read properties
                inputStream = new FileInputStream(f);
                p.load(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("closed");
            // Attempt to close the input stream
            try {
                if(inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return p;
    }

    /**
     * Writes a {@link Properties} file to its set file location
     * @param properties The {@link Properties} file to write
     */
    @SuppressWarnings("unused")
    public static void writeProperties(Properties properties) {
        // Retrieve the file location
        String tokenLocation = System.getProperty("user.home") + "\\WebhookManager\\manager_props.properties";
        File f = new File(tokenLocation);

        // Setup a FileWriter
        FileWriter fileWriter = null;
        try {
            // Create the properties file if it doesn't exist
            if(!f.isFile()) {
                final boolean mkdir = f.getParentFile().mkdir();
                final boolean created = f.createNewFile();
            }

            // Write properties
            fileWriter = new FileWriter(f);
            properties.store(fileWriter, "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Attempt to close the FileWriter
            try {
                if(fileWriter != null)
                    fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the default {@link Properties} for the manager
     * @return A {@link Properties} file with the default properties
     */
    public static Properties getDefaultProperties() {
        Properties p = new Properties();
        p.put("activity-type", "Playing");
        p.put("activity-desc", "with Webhooks");

        return p;
    }

    /**
     * Converts text into an {@link Activity}
     *
     * @param type The type of activity (Playing, Listening, Watching, Competing)
     * @param description The description of the activity (ex. Youtube)
     * @return The created {@link Activity}
     */
    public static Activity makeActivity(String type, String description) {
        if(description == null || description.length() == 0)
            return Activity.playing("with Webhooks");
        return switch (type.toLowerCase()) {
            case "playing" -> Activity.playing(description);
            case "listening" -> Activity.listening(description);
            case "watching" -> Activity.watching(description);
            case "competing" -> Activity.competing(description);
            default -> Activity.playing("with Webhooks");
        };
    }

}
