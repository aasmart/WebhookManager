package other;

import events.Startup;
import guis.MainConsole;
import guis.TokenPopUp;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;

public class WebhookGUI {
    public static final String BASE_URL = "https://discord.com/api/v9";

    public JDA BOT;
    public static WebhookGUI GUI = new WebhookGUI();
    public MainConsole MAIN_CONSOLE;

    public WebhookGUI() {
        // TODO Mac support

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

    public static void main(String[] args) {

    }

    public static String readToken() {
        String tokenLocation = System.getProperty("user.home") + "\\AppData\\Local\\WebhookManager\\bot_token.secret";
        BufferedReader reader = null;
        String token = "";
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

    @SuppressWarnings("unused")
    public static boolean writeToken(String token) {
        String tokenLocation = System.getProperty("user.home") + "\\AppData\\Local\\WebhookManager\\bot_token.secret";
        File f = new File(tokenLocation);
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

    public static boolean sendMessage(String username, String avatar, String message, String id, String token) throws IOException {
        JSONObject body = new JSONObject();
        body.put("allowed_mentions", new JSONObject().put("parse", new JSONArray().putAll(new String[]{"users", "roles", "everyone"})));

        body.put("content", message);
        if(username != null)
            body.put("username", username);
        if(avatar != null) {
            URL url = new URL(avatar);
            BufferedImage img = ImageIO.read(url);
            if(img != null)
                body.put("avatar_url", avatar);
        }

        // Post Request
        RequestBody formBody = RequestBody.create(body.toString(), MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/webhooks" + "/" + id + "/" + token)
                .post(formBody)
                .build();

        Call call = new OkHttpClient().newCall(request);
        return call.execute().isSuccessful();
    }
}
