package me.minercoffee.simpleminecraftbot.framework.commands.event;

import me.minercoffee.simpleminecraftbot.framework.PluginEvent;
import me.minercoffee.simpleminecraftbot.framework.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubCommandLogEvent extends PluginEvent {
    private final CommandSender sender;
    private final String[] arguments;
    private final SubCommand command;
    private boolean cancelled;

    public Player getPlayer() throws ClassCastException {
        return (Player)this.sender;
    }

    public SubCommandLogEvent(CommandSender sender, String[] arguments, SubCommand command) {
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

    public SubCommand getCommand() {
        return this.command;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
