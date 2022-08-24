package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Staffplaytime implements TabExecutor {
    private final Main plugin;
    public Staffplaytime(Main plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof RemoteConsoleCommandSender) {
            if (args[0].equalsIgnoreCase("report")) {
                StringBuilder toSend = new StringBuilder();
                try {
                    for (String uuid : Main.getInstance().getConfig().getKeys(false)) {
                        toSend.append("- **").append(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).append("** has played for **").append(plugin.convertTime(Main.getInstance().getConfig().getLong(uuid))).append("** this week\n");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                plugin.playerLogListener.saveAllPlayers();
                System.out.println(toSend);
                plugin.sendstaffonline(Bukkit.getOfflinePlayer("MinerCoffee97"), "**WEEKLY SUMMARY**\n" + toSend, false, Color.GRAY);
            }
            if (args[0].equalsIgnoreCase("reset")) {
                // clear the config
                for (String key : Main.getInstance().getConfig().getKeys(false)) {
                    plugin.playerLogListener.saveAllPlayers();
                    Main.getInstance().getConfig().set(key, null);
                    plugin.saveConfig();
                    Main.ConfigUpdater();
                }
            }
        } else if (sender instanceof Player player){
            if (label.equalsIgnoreCase("staffplaytime") && player.isOp()) {
                if (args[0].equalsIgnoreCase("report")) {
                    StringBuilder toSend = new StringBuilder();
                    try {
                        for (String uuid : Main.getInstance().getConfig().getKeys(false)) {
                            toSend.append("- **").append(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).append("** has played for **").append(plugin.convertTime(Main.getInstance().getConfig().getLong(uuid))).append("** this week\n");
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    plugin.playerLogListener.saveAllPlayers();
                    System.out.println(toSend);
                 plugin.sendstaffonline(Bukkit.getOfflinePlayer("MinerCoffee97"), "**WEEKLY SUMMARY**\n" + toSend, false, Color.GRAY);
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    // clear the config
                    for (String key : Main.getInstance().getConfig().getKeys(false)) {
                        plugin.playerLogListener.saveAllPlayers();
                        Main.getInstance().getConfig().set(key, null);
                        plugin.saveConfig();
                        Main.ConfigUpdater();

                    }
                }
            }
        }
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsArguements = new ArrayList<>();
            subcommandsArguements.add("report");
            subcommandsArguements.add("reset");
            return subcommandsArguements;
        }
        return null;
    }
}
