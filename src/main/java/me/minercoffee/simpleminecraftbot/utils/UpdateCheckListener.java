package me.minercoffee.simpleminecraftbot.utils;

import com.jeff_media.updatechecker.UpdateCheckEvent;
import com.jeff_media.updatechecker.UpdateCheckResult;
import com.jeff_media.updatechecker.UpdateCheckSuccess;
import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class UpdateCheckListener implements Listener {
    private final Main plugin;
    public UpdateCheckListener(Main plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onUpdateCheck(@NotNull UpdateCheckEvent event) {
        if(event.getRequesters()==null) return;

        for(CommandSender sender : event.getRequesters()) {
            plugin.getLogger().info(sender.getName() + " has requested an update check!");
        }

        if(event.getSuccess() == UpdateCheckSuccess.FAIL) {
            plugin.getLogger().info("it failed, lol");
        } else if(event.getResult() == UpdateCheckResult.NEW_VERSION_AVAILABLE) {
            plugin.getLogger().info("there is a new version available: " + event.getLatestVersion());
        } else {
            plugin.getLogger().info("you are running the latest version :)");
        }

    }
}