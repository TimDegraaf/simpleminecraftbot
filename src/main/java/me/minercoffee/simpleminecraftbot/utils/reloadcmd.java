package me.minercoffee.simpleminecraftbot.utils;

import games.negative.framework.command.annotation.CommandInfo;
import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandInfo(
        name = "sbreload",
        permission = "simpleminecraftbot.admin",
        args = {"player"}
)
public class reloadcmd implements CommandExecutor {

    public reloadcmd(Main plugin) {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("sbreload")) {
            if (p.isOp() | p.hasPermission("simpleminecraftbot.admin")) {
                Main.instance.reloadConfig();
                p.sendMessage(ColorMsg.color("&c&l Reloaded the config"));
                System.out.println(ColorMsg.color("&c&l Reloaded the config"));
            }
        }
        return true;
    }
}
