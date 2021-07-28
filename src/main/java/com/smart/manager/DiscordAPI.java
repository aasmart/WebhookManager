package com.smart.manager;

import com.smart.manager.utils.OctetBody;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

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
    public static boolean sendMessage(String username, String avatar, String message, List<File> attachments, String id, String token) throws IOException {
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

        // Special case for when attachments are present
        if(attachments.size() > 0) {
            final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for(int i = 0; i < attachments.size(); i++) {
                try {
                    byte[] bytes = new FileInputStream(attachments.get(i)).readAllBytes();
                    builder.addFormDataPart("file" + i, attachments.get(i).getName(), new OctetBody(bytes));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Request request = new Request.Builder()
                    .url(BASE_URL + "/webhooks" + "/" + id + "/" + token)
                    .post(builder.addFormDataPart("payload_json", body.toString()).build())
                    .build();

            Call call = new OkHttpClient().newCall(request);
            Response r = call.execute();
            return r.isSuccessful();
        } else {
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
}
