package de.rechergg;

import de.rechergg.backend.BackendAPI;
import de.rechergg.backend.RequestManager;
import de.rechergg.command.CommandHandler;
import de.rechergg.command.CommandManager;
import de.rechergg.i18n.Translations;
import de.rechergg.util.EnvManager;
import de.rechergg.util.Health;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class DevCordBot {

    private final Logger logger;
    private final Translations translations;
    private final CommandManager commandManager;
    private final RequestManager requestManager;
    private BackendAPI backendAPI;
    private JDA jda;

    public DevCordBot() {
        this.logger = LoggerFactory.getLogger(DevCordBot.class);
        this.translations = new Translations();

        String token = System.getenv("DEVMARKT_BOT_TOKEN");
        if (token == null) {
            this.logger.error(translations().loggerTranslation().get("token.notfound"));
            System.exit(-1);
        }

        new EnvManager(this);

        this.commandManager = new CommandManager(this);
        this.requestManager = new RequestManager(this);
        login(token);
    }

    private void login(String token) {
        this.logger.info(translations().loggerTranslation().get("bot.login"));
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setAutoReconnect(true);
        builder.addEventListeners(new CommandHandler(this));
        builder.setBulkDeleteSplittingEnabled(false);
        try {
            this.jda = builder.build().awaitReady();
        } catch (InvalidTokenException | InterruptedException e) {
            this.logger.error(translations().loggerTranslation().get("token.wrong"));
            this.logger.error(translations().loggerTranslation().get("token.current", token));
            this.logger.error(translations().loggerTranslation().get("exception", e.getMessage(), e.getClass().getSimpleName()));
            System.exit(-1);
        }

        this.commandManager.invoke();
        this.requestManager.invoke();

        this.backendAPI = new BackendAPI(this);

        while (!this.backendAPI.statusController().loopStarted()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (this.backendAPI.statusController().status() != Health.UP) {
            this.logger.warn(translations().loggerTranslation().get("backend.down", this.backendAPI.statusController().status()));
            System.exit(-1);
        }

        this.logger.info(translations().loggerTranslation().get("bot.logged.in", this.jda.getSelfUser().getName()));

        this.jda.getPresence().setStatus(OnlineStatus.ONLINE);
        this.jda.getPresence().setActivity(Activity.playing(translations().messageTranslation().get("discord.activity", Locale.GERMAN)));
    }

    public Logger logger() {
        return logger;
    }

    public JDA jda() {
        return jda;
    }

    public Translations translations() {
        return translations;
    }

    public CommandManager commandManager() {
        return commandManager;
    }

    public RequestManager requestManager() {
        return requestManager;
    }
}
