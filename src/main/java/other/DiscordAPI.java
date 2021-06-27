package other;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * The class for housing the various
 */
public class DiscordAPI {
    /**
     * The base URL for HTTP Requests
     */
    public static final String BASE_URL = "https://discord.com/api/v9";

    /**
     * Creates a POST request to send a message
     *
     * @param username The username of the {@link Webhook}
     * @param avatar The avatar of the {@link Webhook}
     * @param message The contents of the message
     * @param id The ID of the {@link Webhook}
     * @param token The token of the {@link Webhook}
     * @return A boolean for indicating if the message was sent
     * @throws IOException If there was an error executing the HTTP Request
     */
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
