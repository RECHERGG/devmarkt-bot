package de.rechergg.backend;

import de.rechergg.DevCordBot;
import de.rechergg.backend.impl.v1.response.health.StatusController;

public class BackendAPI {

    private final DevCordBot devCordBot;
    private StatusController statusController;

    public BackendAPI(DevCordBot devCordBot) {
        this.devCordBot = devCordBot;

        invoke();
    }

    private void invoke() {
        this.statusController = new StatusController(this.devCordBot);
    }

    public StatusController statusController() {
        return statusController;
    }
}
