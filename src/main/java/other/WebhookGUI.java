package other;

import events.Startup;
import guiComponents.guis.MainConsole;
import guiComponents.popups.TokenPopUp;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.*;

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
     * The constructor for setting up the WebhookGUI
     */
    public WebhookGUI() {
        // Read bot token
        String token = readToken();
        if(token.length() == 0)
            return;

        try {
            JDABuilder builder = JDABuilder.createDefault(token)
                    .addEventListeners(new Startup())
                    .setRawEventsEnabled(true)
                    .setActivity(Activity.playing("with webhooks"));

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

}
