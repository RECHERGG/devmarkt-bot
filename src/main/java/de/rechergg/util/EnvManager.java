package de.rechergg.util;

import de.rechergg.DevCordBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvManager {

    private final DevCordBot devCordBot;
    private final Logger logger;
    public static final String DEVMARKT_BACKEND_HOST = System.getenv("DEVMARKT_BACKEND_HOST");
    public static final String DEVMARKT_BACKEND_PORT = System.getenv("DEVMARKT_BACKEND_PORT");
    public static final String DEVMARKT_BACKEND_URL = "http://" + DEVMARKT_BACKEND_HOST + ":" + DEVMARKT_BACKEND_PORT;

    public EnvManager(DevCordBot devCordBot) {
        this.devCordBot = devCordBot;
        this.logger = LoggerFactory.getLogger(EnvManager.class);

        invoke();
    }

    private void invoke() {
        if (DEVMARKT_BACKEND_HOST != null && DEVMARKT_BACKEND_PORT != null) return;

        if (DEVMARKT_BACKEND_HOST == null) {
            this.logger.error(this.devCordBot.translations().loggerTranslation().get("env.host"));
        }

        if (DEVMARKT_BACKEND_PORT == null) {
            this.logger.error(this.devCordBot.translations().loggerTranslation().get("env.port"));
        }

        System.exit(-1);
    }
}
