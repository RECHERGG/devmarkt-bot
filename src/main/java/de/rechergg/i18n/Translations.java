package de.rechergg.i18n;

import de.rechergg.DevCordBot;

public class Translations {

    private final LoggerTranslation loggerTranslation;
    private final MessageTranslation messageTranslation;

    public Translations() {
        this.loggerTranslation = new LoggerTranslation();
        this.messageTranslation = new MessageTranslation();
    }

    public LoggerTranslation loggerTranslation() {
        return loggerTranslation;
    }

    public MessageTranslation messageTranslation() {
        return messageTranslation;
    }
}
