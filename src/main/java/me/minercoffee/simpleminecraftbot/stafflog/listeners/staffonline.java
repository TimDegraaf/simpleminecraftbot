package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.utils.ColorMsg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class staffonline implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        ArrayList<Player> list = new ArrayList<>(player.getServer().getOnlinePlayers());
        if (command.getName().equalsIgnoreCase("staff-all")) {
            if (player.isOp() | player.hasPermission("illusive.staff")) {
                for (Player staff : list) {
                    if (staff.isOp() | staff.hasPermission("illusive.staff")) {
                        player.sendMessage((ColorMsg.color("&lThere are " + staff.getName() + " available for staff duties")));
                    }
                }
            }
        }
        return true;
    }
}