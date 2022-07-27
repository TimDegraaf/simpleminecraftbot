package me.minercoffee.simpleminecraftbot.afk.command;

import me.minercoffee.simpleminecraftbot.afk.AFKManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AFKCommand implements CommandExecutor {

    private final AFKManager afkManager;

    public AFKCommand(AFKManager afkManager) {
        this.afkManager = afkManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (sender instanceof Player p && p.hasPermission("illusive.staff")){

            if(afkManager.toggleAFKStatus(p)){

                p.sendMessage("You are now AFK.");

                afkManager.announceToOthers(p, true);

            }else{
                p.sendMessage("You are no longer AFK.");

                afkManager.announceToOthers(p, false);
            }

        }

        return true;
    }
}