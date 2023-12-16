package de.rechergg.backend;

import com.google.gson.JsonParser;
import de.rechergg.DevCordBot;
import de.rechergg.backend.impl.v1.response.health.HealthResponse;
import de.rechergg.util.EnvManager;
import de.rechergg.util.HTTPMethod;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class RequestManager {

    private final DevCordBot devCordBot;
    private final OkHttpClient client;
    private HealthResponse health;

    public RequestManager(DevCordBot devCordBot) {
        this.devCordBot = devCordBot;
        this.client = new OkHttpClient();
    }

    public void invoke() {
        //add requests
        this.health = new HealthResponse(this.devCordBot);


    }

    public CompletableFuture<Object> sendRequest(@NotNull String path, @NotNull HTTPMethod method) {
        return this.sendRequest(path, method, (response, future) -> {
            if (future.isCompletedExceptionally()) {
                try {
                    future.get();
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
                return;
            }

            if (response.body() == null) {
                future.completeExceptionally(new RuntimeException("Error code " + response.code() + ": null"));
                return;
            }

            if (response.code() != 200) {
                try {
                    future.completeExceptionally(new RuntimeException("Error code " + response.code() + ": " + response.body().string()));
                } catch (IOException e) {
                    future.completeExceptionally(e);
                }
                return;
            }

            try {
                future.complete(JsonParser.parseString(response.body().string()).getAsJsonObject());
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });
    }

    private CompletableFuture<Object> sendRequest(@NotNull String path, @NotNull HTTPMethod method,
                                                  @NotNull BiConsumer<Response, CompletableFuture<Object>> consumer) {
        CompletableFuture<Object> future = new CompletableFuture<>();

        Request.Builder builder = new Request.Builder()
                .url(EnvManager.DEVMARKT_BACKEND_URL + path);
                //.header("Authorization", this.spotify.getRequest().getConfig().getSpotifyAccessToken());

        switch (method) {
            case POST -> builder.post(RequestBody.create(new byte[0]));
            case PUT -> builder.put(RequestBody.create(new byte[0]));
            case GET -> builder.get();
        }

        this.client.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                consumer.accept(response, future);
            }
        });
        return future;
    }

    public HealthResponse health() {
        return health;
    }
}
