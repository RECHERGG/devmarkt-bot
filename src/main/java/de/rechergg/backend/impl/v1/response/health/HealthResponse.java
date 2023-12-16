package de.rechergg.backend.impl.v1.response.health;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.rechergg.DevCordBot;
import de.rechergg.backend.CustomResponse;
import de.rechergg.util.HTTPMethod;
import de.rechergg.util.Health;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HealthResponse extends CustomResponse {


    public HealthResponse(DevCordBot devCordBot) {
        super(devCordBot, HTTPMethod.GET, "/health");
    }

    @Override
    public void handelException(@NotNull CompletableFuture<Object> future) {
        future.exceptionally(e -> {
            devCordBot().logger().error(devCordBot().translations().loggerTranslation().get("exception", e.getMessage(), e.getClass().getSimpleName()));
            return null;
        });
    }

    /*
     * Request a new Health check
     */

    public Health status() {
        CompletableFuture<Object> future = devCordBot().requestManager().sendRequest(path(), httpMethod());

        if (future.isCompletedExceptionally()) {
            handelException(future);
            return Health.DOWN;
        }

        try {
            JsonObject body = JsonParser.parseString(future.get().toString()).getAsJsonObject();
            if (!body.has("status")) return Health.DOWN;
            return Health.valueOf(body.get("status").getAsString());
        } catch (InterruptedException | ExecutionException e) {
            handelException(future);
            return Health.DOWN;
        }
    }

}
