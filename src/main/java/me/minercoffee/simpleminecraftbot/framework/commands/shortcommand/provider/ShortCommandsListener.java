package me.minercoffee.simpleminecraftbot.framework.commands.shortcommand.provider;

import me.minercoffee.simpleminecraftbot.framework.commands.shortcommand.ShortCommands;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;

public class ShortCommandsListener implements Listener {
    private final ShortCommands shortCommands;

    public ShortCommandsListener() {
        new ShortCommandsProvider();
        this.shortCommands = ShortCommands.getInstance();
    }

    @EventHandler
    public void onCommandInput(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        message = message.replaceFirst("/", "");
        String[] args = message.split(" ");
        String cmd = args[0];
        this.shortCommands.getCommand(cmd).ifPresent((command) -> {
            if (!event.isCancelled()) {
                String[] newArgs = (String[]) Arrays.copyOfRange(args, 1, args.length);
                command.execute(event.getPlayer(), (String)null, newArgs);
                event.setCancelled(true);
            }
        });
        this.shortCommands.getSubCommand(cmd).ifPresent((subCommand) -> {
            if (!event.isCancelled()) {
                String[] newArgs = (String[])Arrays.copyOfRange(args, 1, args.length);
                subCommand.execute(event.getPlayer(), newArgs);
                event.setCancelled(true);
            }
        });
    }
}
