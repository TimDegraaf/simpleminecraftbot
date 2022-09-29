package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.Main;
import me.minercoffee.simpleminecraftbot.utils.Embles;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.minercoffee.simpleminecraftbot.utils.DataManager.getStaffplaytimeConfig;
import static me.minercoffee.simpleminecraftbot.utils.DataManager.savestaffplaytime;

public class Staffplaytime implements TabExecutor {
    private final Embles embles;
    private final Main plugin;
    public Staffplaytime(Main plugin, Embles embles){
        this.plugin = plugin;
        this.embles = embles;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            if (args[0].equalsIgnoreCase("report")) {
                StringBuilder toSend = new StringBuilder();
                try {
                    for (String uuid : getStaffplaytimeConfig().getKeys(false)) {
                        toSend.append("- **").append(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).append("** has played for **").append(plugin.convertTime(getStaffplaytimeConfig().getLong(uuid))).append("** this week\n");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                plugin.playerLogListener.saveAllPlayers();
                System.out.println(toSend);
                embles.sendstaffonline(Bukkit.getOfflinePlayer("MinerCoffee97"), "**WEEKLY SUMMARY**\n" + toSend, false, Color.GRAY);
            }
            if (args[0].equalsIgnoreCase("reset")) {
                for (String key : getStaffplaytimeConfig().getKeys(false)) {
                    plugin.playerLogListener.saveAllPlayers();
                   getStaffplaytimeConfig().set(key, null);
                   savestaffplaytime();
                }
            }
        } else {
            if (label.equalsIgnoreCase("staffplaytime") && player.isOp()) {
                if (args[0].equalsIgnoreCase("report")) {
                    StringBuilder toSend = new StringBuilder();
                    try {
                        for (String uuid : getStaffplaytimeConfig().getKeys(false)) {
                            toSend.append("- **").append(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).append("** has played for **").append(plugin.convertTime(getStaffplaytimeConfig().getLong(uuid))).append("** this week\n");
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    plugin.playerLogListener.saveAllPlayers();
                    System.out.println(toSend);
                 embles.sendstaffonline(Bukkit.getOfflinePlayer("MinerCoffee97"), "**WEEKLY SUMMARY**\n" + toSend, false, Color.GRAY);
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    for (String key : getStaffplaytimeConfig().getKeys(false)) {
                        plugin.playerLogListener.saveAllPlayers();
                        getStaffplaytimeConfig().set(key, null);
                        savestaffplaytime();

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
