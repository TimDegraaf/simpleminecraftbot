package me.minercoffee.simpleminecraftbot.stafflog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.minercoffee.simpleminecraftbot.utils.DataManager.*;

public class staffhomes implements TabExecutor {
    public staffhomes() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (command.getName().equalsIgnoreCase("staffhome")){
                if (p.hasPermission("simpleminecraftbot.staff") || p.isOp()) {
                    if (args.length == 1 && args[0].equalsIgnoreCase("set")) {
                        if (getDataConfig().isConfigurationSection("savedlocations." + p.getName())) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', getDataConfig().getString("override-message") + getDataConfig().getInt("savedlocations." + p.getName() + ".x") + " " + getDataConfig().getInt("savedlocations." + p.getName() + ".y") + " " + getDataConfig().getInt("savedlocations." + p.getName() + ".z")));
                            saveLocation(p);
                        } else {
                            saveLocation(p);
                        }
                    } else if (args.length == 1 && args[0].equalsIgnoreCase("return")) {
                        if (getDataConfig().isConfigurationSection("savedlocations." + p.getName())) {
                            Location return_location = new Location(p.getWorld(), getDataConfig().getInt("savedlocations." + p.getName() + ".x"), getDataConfig().getInt("savedlocations." + p.getName() + ".y"), getDataConfig().getInt("savedlocations." + p.getName() + ".z"));
                            p.teleport(return_location);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getMessagesConfig().getString("return-message")))); //TODO switch it to the message.yml
                            getDataConfig().set("savedlocations." + p.getName(), null);
                            SaveData();
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "You never set a staff home.");
                        }
                    } else if (args.length == 1) {
                        if (p.hasPermission("simpleminecraftbot.staff") || p.isOp()) {
                            Player t = Bukkit.getPlayer(args[0]);
                            if (!(t == null)) {
                                if (getDataConfig().isConfigurationSection("savedlocations." + t.getName())) {
                                    p.sendMessage(ChatColor.GREEN + "Teleporting to temporary staff home(" + t.getName() + ") @: " + ChatColor.GRAY + getDataConfig().getInt("savedlocations." + t.getName() + ".x") + " " + getDataConfig().getInt("savedlocations." + t.getName() + ".y") + " " + getDataConfig().getInt("savedlocations." + t.getName() + ".z"));
                                    Location return_location = new Location(t.getWorld(), getDataConfig().getInt("savedlocations." + t.getName() + ".x"), getDataConfig().getInt("savedlocations." + t.getName() + ".y"), getDataConfig().getInt("savedlocations." + t.getName() + ".z"));
                                    p.teleport(return_location);
                                } else {
                                    p.sendMessage(ChatColor.DARK_RED + "That player does not have a home set.");
                                }

                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7==&a&lStaff&eHomes&7 by MinerCoffee=="));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome set &7- &9Set a Temporary Home"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome return &7- &9Return to Home and Remove it"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome <name> &7- &9Teleport to a temporary home"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7========================="));
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
                }
            } else {
                System.out.println("A player must execute this command.");
            }
        }
        return true;
    }

    private void saveLocation(@NotNull Player p) {
        Location l = p.getLocation();
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("set-message") + Math.round(l.getX()) + " " + Math.round(l.getY()) + " " + Math.round(l.getZ()))); //TODO Switch message.yml
        getDataConfig().createSection("savedlocations." + p.getName());
        getDataConfig().set("savedlocations." + p.getName() + ".x", l.getX());
        getDataConfig().set("savedlocations." + p.getName() + ".y", l.getY());
        getDataConfig().set("savedlocations." + p.getName() + ".z", l.getZ());
        SaveData();
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> playerNames = new ArrayList<>();
        if (args.length == 1) {
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player player : players) {
                playerNames.add(player.getName());
            }
            ArrayList<String> subcommandsArguements = new ArrayList<>();
            subcommandsArguements.add("set");
            subcommandsArguements.add("return");
            return subcommandsArguements;
        }
        return playerNames;
    }
}
