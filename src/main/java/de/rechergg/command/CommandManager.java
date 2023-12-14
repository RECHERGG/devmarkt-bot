package de.rechergg.command;

import de.rechergg.DevCordBot;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final DevCordBot devCordBot;

    private final List<Command> commands;

    public CommandManager(DevCordBot devCordBot) {
        this.devCordBot = devCordBot;
        this.commands = new ArrayList<>();
    }

    private void registerCommand(@NotNull Command command) {
        this.commands.add(command);
    }

    public void invoke() {
        //register commands

        CommandListUpdateAction update = this.devCordBot.jda().updateCommands();

        for (Command command : commands) {
            update = update.addCommands(command.getCommand());
        }

        update.queue();
    }

    @NotNull
    public List<Command> commands() {
        return commands;
    }
}