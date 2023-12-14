package de.rechergg.command;

import de.rechergg.DevCordBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class Command {

    private final DevCordBot devCordBot;

    public Command(DevCordBot devCordBot) {
        this.devCordBot = devCordBot;
    }

    public abstract CommandData getCommand();

    public abstract void handle(SlashCommandInteractionEvent event);

    public DevCordBot devCordBot() {
        return devCordBot;
    }
}