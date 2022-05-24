package me.minercoffee.simpleminecraftbot.framework.commands.event;

import me.minercoffee.simpleminecraftbot.framework.PluginEvent;
import me.minercoffee.simpleminecraftbot.framework.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLogEvent extends PluginEvent {
    private final CommandSender sender;
    private final String[] arguments;
    private final Command command;
    private boolean cancelled;

    public Player getPlayer() throws ClassCastException {
        return (Player)this.sender;
    }

    public CommandLogEvent(CommandSender sender, String[] arguments, Command command) {
        this.sender = sender;
        this.arguments = arguments;
        this.command = command;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public String[] getArguments() {
        return this.arguments;
    }

    public Command getCommand() {
        return this.command;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
