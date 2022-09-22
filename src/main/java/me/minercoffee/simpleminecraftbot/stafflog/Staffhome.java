package me.minercoffee.simpleminecraftbot.stafflog;
import me.minercoffee.simpleminecraftbot.utils.ColorMsg;
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

public class Staffhome implements TabExecutor {
    public Staffhome() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (command.getName().equalsIgnoreCase("staffhome")) {
                if (p.hasPermission("simpleminecraftbot.staff") || p.isOp()) {
                    if (args.length == 1 && args[0].equalsIgnoreCase("set")) {

                        if (getDataConfig().isConfigurationSection("staff-homes-locations." + p.getName())) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("override-message") + getDataConfig().getInt("staff-homes-locations." + p.getName() + ".x") + " " + getDataConfig().getInt("staff-homes-locations." + p.getName() + ".y") + " " + getDataConfig().getInt("staff-homes-locations." + p.getName() + ".z")));
                            saveLocation(p);
                        } else {
                            saveLocation(p);
                        }
                    } else if (args.length == 1 && args[0].equalsIgnoreCase("return")) {
                        if (getDataConfig().isConfigurationSection("staff-homes-locations." + p.getName())) {
                            Location return_location = new Location(p.getWorld(), getDataConfig().getInt("staff-homes-locations." + p.getName() + ".x"), getDataConfig().getInt("staff-homes-locations." + p.getName() + ".y"), getDataConfig().getInt("staff-homes-locations." + p.getName() + ".z"));
                            p.teleport(return_location);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getMessagesConfig().getString("return-message")))); //TODO switch it to the message.yml
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "You never set a staff home.");
                        }
                    } else if (args.length == 1) {
                        if (p.hasPermission("simpleminecraftbot.staff") || p.isOp()) {
                            Player t = Bukkit.getPlayer(args[0]);
                            if (!(t == null)) {
                                if (getDataConfig().isConfigurationSection("staff-homes-locations." + t.getName())) {
                                    p.sendMessage(ChatColor.GREEN + "Teleporting to temporary staff home(" + t.getName() + ") @: " + ChatColor.GRAY + getDataConfig().getInt("staff-homes-locations." + t.getName() + ".x") + " " + getDataConfig().getInt("staff-homes-locations." + t.getName() + ".y") + " " + getDataConfig().getInt("staff-homes-locations." + t.getName() + ".z"));
                                    Location return_location = new Location(t.getWorld(), getDataConfig().getInt("staff-homes-locations." + t.getName() + ".x"), getDataConfig().getInt("staff-homes-locations." + t.getName() + ".y"), getDataConfig().getInt("staff-homes-locations." + t.getName() + ".z"));
                                    p.teleport(return_location);
                                } else {
                                    p.sendMessage(ChatColor.DARK_RED + "That player does not have a home set.");
                                }

                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
                        }
                    }
                    if (args.length == 1 && args[0].equalsIgnoreCase("delete")) {
                        if (p.hasPermission("simpleminecraftbot.staff") || p.isOp()) {
                            getDataConfig().set("staff-homes-locations." + p.getName(), null); //deletes the home
                            SaveData();
                            p.sendMessage(ColorMsg.color("&c&lStaff Home Deleted!"));
                        }
                    }
                    if (p.hasPermission("simpleminecraftbot.staff") || p.isOp()) {
                        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7==&a&lStaff&eHomes&7 by MinerCoffee=="));
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome set &7- &9Set a Temporary Home"));
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome return &7- &9Return to Home"));
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome delete &7- &9Removes your home"));
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7========================="));
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
                }
            }
        }
        return true;
    }

    private void saveLocation(@NotNull Player p) {
        Location l = p.getLocation();
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("set-message") + Math.round(l.getX()) + " " + Math.round(l.getY()) + " " + Math.round(l.getZ()))); //TODO Switch message.yml
        getDataConfig().createSection("staff-homes-locations." + p.getName());
        getDataConfig().set("staff-homes-locations." + p.getName() + ".x", l.getX());
        getDataConfig().set("staff-homes-locations." + p.getName() + ".y", l.getY());
        getDataConfig().set("staff-homes-locations." + p.getName() + ".z", l.getZ());
        SaveData();
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsArguements = new ArrayList<>();
            subcommandsArguements.add("set");
            subcommandsArguements.add("return");
            subcommandsArguements.add("delete");
            subcommandsArguements.add("help");
            return subcommandsArguements;
        }
        if (args.length == 2) {
            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player value : players) {
                playerNames.add(value.getName());
            }
            return playerNames;
        }
        return null;
    }
}