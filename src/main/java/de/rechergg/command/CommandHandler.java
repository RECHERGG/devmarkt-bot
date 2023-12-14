package de.rechergg.command;

import de.rechergg.DevCordBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandHandler extends ListenerAdapter {

    private final DevCordBot devCordBot;

    public CommandHandler(DevCordBot devCordBot) {
        this.devCordBot = devCordBot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        this.devCordBot.commandManager().commands()
                .stream()
                .filter(command -> command.getCommand().getName().equalsIgnoreCase(event.getInteraction().getName()))
                .findFirst()
                .ifPresent(command -> command.handle(event));
    }
}