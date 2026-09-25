package com.jarvis.app;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiApiService {

    private static final String API_KEY = "BURAYA_API_ANAHTARINIZI_YAPISTIRIN";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;
    private static final String SYSTEM_PROMPT = "Sen Jarvis'sin, kullanıcının kişisel AI asistanısın. Türkçe konuş, kısa ve net cevaplar ver. Iron Man'deki Jarvis gibi profesyonel ama samimi ol.";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();

    public interface GeminiCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public static void sendMessage(String userMessage, GeminiCallback callback) {
        try {
            JSONObject userPart = new JSONObject();
            userPart.put("text", SYSTEM_PROMPT + "\n\nKullanıcı: " + userMessage);

            JSONArray userParts = new JSONArray();
            userParts.put(userPart);

            JSONObject userContent = new JSONObject();
            userContent.put("role", "user");
            userContent.put("parts", userParts);

            JSONArray contents = new JSONArray();
            contents.put(userContent);

            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", contents);

            RequestBody body = RequestBody.create(requestBody.toString(), JSON);
            Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            String text = jsonResponse
                                .getJSONArray("candidates")
                                .getJSONObject(0)
                                .getJSONObject("content")
                                .getJSONArray("parts")
                                .getJSONObject(0)
                                .getString("text");
                            callback.onSuccess(text);
                        } catch (Exception e) {
                            callback.onError("JSON parse hatası: " + e.getMessage());
                        }
                    } else {
                        callback.onError("API hatası: " + response.code());
                    }
                }
            });
        } catch (Exception e) {
            callback.onError("İstek oluşturma hatası: " + e.getMessage());
        }
    }
}
