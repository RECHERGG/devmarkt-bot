package de.rechergg.backend;

import de.rechergg.DevCordBot;
import de.rechergg.util.HTTPMethod;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class CustomResponse {

    private final DevCordBot devCordBot;
    private final HTTPMethod httpMethod;
    private final String path;

    public CustomResponse(DevCordBot devCordBot, HTTPMethod httpMethod, String path) {
        this.devCordBot = devCordBot;
        this.httpMethod = httpMethod;
        this.path = path;
    }

    public abstract void handelException(@NotNull CompletableFuture<Object> future);

    public DevCordBot devCordBot() {
        return devCordBot;
    }

    public HTTPMethod httpMethod() {
        return httpMethod;
    }

    public String path() {
        return path;
    }
}
