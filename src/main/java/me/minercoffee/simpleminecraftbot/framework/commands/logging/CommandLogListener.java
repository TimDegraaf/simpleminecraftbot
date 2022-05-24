package me.minercoffee.simpleminecraftbot.framework.commands.logging;

import me.minercoffee.simpleminecraftbot.framework.commands.event.CommandLogEvent;
import me.minercoffee.simpleminecraftbot.framework.commands.event.SubCommandLogEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CommandLogListener implements Listener {
    public CommandLogListener() {
    }

    @EventHandler
    public void onCommandLog(CommandLogEvent event) {
        event.getCommand().getLogEvent().accept(event);
    }

    @EventHandler
    public void onSubCommandLog(SubCommandLogEvent event) {
        event.getCommand().getSubCommandLogEvent().accept(event);
    }
}
