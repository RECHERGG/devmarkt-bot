package de.rechergg.backend.impl.v1.response.health;

import de.rechergg.DevCordBot;
import de.rechergg.util.Health;
import net.dv8tion.jda.api.OnlineStatus;

public class StatusController {

    private final DevCordBot devCordBot;

    private volatile Health status = Health.DOWN;
    private boolean loopStarted = false;

    public StatusController(DevCordBot devCordBot) {
        this.devCordBot = devCordBot;

        loop();
    }

    public void loop() {
        new Thread(() -> {
            loopStarted = true;
            while (true) {
                this.status = this.devCordBot.requestManager().health().status();

                switch (status()) {
                    case UP -> this.devCordBot.jda().getPresence().setStatus(OnlineStatus.ONLINE);
                    case DOWN -> this.devCordBot.jda().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
     * Gives the Status from the last request
     */
    public Health status() {
        return status;
    }

    public boolean loopStarted() {
        return loopStarted;
    }
}
