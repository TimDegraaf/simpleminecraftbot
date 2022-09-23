package me.minercoffee.simpleminecraftbot;

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

import static me.minercoffee.simpleminecraftbot.utils.DataManager.*;

public class Homes implements TabExecutor {
    private String homeName;

    public Homes() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (command.getName().equalsIgnoreCase("staffhome")) {
                if (p.hasPermission("simpleminecraftbot.staff") || p.isOp()) {
                    if (args.length == 0) {
                        p.sendMessage(ColorMsg.color("&lPLease perform:  &r&7/staffhome help "));
                        return true;
                    }
                    StringBuilder builder = new StringBuilder();
                    for (String arg : args) {
                        builder.append(arg);
                        builder.append(" ");
                    }
                    homeName = builder.toString();
                    homeName = homeName.stripTrailing();
/*                     if (args.length == 1 && args[0].equalsIgnoreCase("return")) { //teleports owner to homeName
                         if (getDataConfig().isConfigurationSection("staff-homes-locations." +  homeName)) {
                             System.out.println("test");
                             Location return_location = new Location(p.getWorld(), getDataConfig().getInt("staff-homes-locations." + homeName + ".x"), //TODO Added Pinch & YAW & World. Ex: world_the_nether
                                     getDataConfig().getInt("staff-homes-locations." +  homeName  + ".y")
                                     , getDataConfig().getInt("staff-homes-locations." +  homeName + ".z"));
                             p.teleport(return_location);
                             p.sendMessage(ColorMsg.color(getMessagesConfig().getString("return-message")));
                         } else {
                             p.sendMessage(ChatColor.DARK_RED + "You never set a staff home.");
                         }
                    } */

                    if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7==&a&lStaff&eHomes&7 by MinerCoffee=="));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome set <name> &7- &9Set a Temporary Home"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome return &7- &9Return to Home"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome delete homeName &7- &9Removes your home"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7========================="));
                        return true;
                    }
                    if (args.length == 1 && args[0].equalsIgnoreCase("return")) {
                        return true;
                    }
                    if (args.length == 1 && args[0].equalsIgnoreCase("set")) {
                        p.sendMessage(ColorMsg.color("&cPlease provide a name. Ex: /staffhome set <name>"));
                        return true;
                    }

                    if (args.length == 1 && args[0].equalsIgnoreCase("delete")) {
                        String HomeName = getDataConfig().getString("staff-homes-locations." + homeName + ".homename");
                        p.sendMessage(ColorMsg.color("&cPlease provide a your" + " " +  HomeName + " " + "with the homeName you created."));
                        return true;
                    }
                   homeName = args[1];
                    if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
                        if (getDataConfig().isConfigurationSection("staff-homes-locations." +  homeName)) {
                            p.sendMessage(ColorMsg.color(getMessagesConfig().getString("override-message")) +
                                    getDataConfig().getInt("staff-homes-locations." +  homeName + ".x") + " " +
                                    getDataConfig().getInt("staff-homes-locations." +  homeName + ".y")
                                    + " " + getDataConfig().getInt("staff-homes-locations." +  homeName  +".z") + " " + getDataConfig().getInt("staff-homes-locations." +  homeName +  ".pinch") + " " +
                                    getDataConfig().getInt("staff-homes-locations." +  homeName + ".yaw")); //TODO Added Pinch & YAW & World. Ex: world_the_nether
                            saveLocation(p);
                        } else {
                            saveLocation(p);
                        }
                    }
                    if (args.length == 3 && args[0].equalsIgnoreCase("return")) { //staffhome return <target> homeName
                        if (p.hasPermission("simpleminecraftbot.staff") || p.isOp()) {
                            Player t = Bukkit.getPlayer(args[1]);
                            String worldName = getDataConfig().getString("staff-homes-locations." + homeName + ".worldName");
                            String HomeName = getDataConfig().getString("staff-homes-locations." + homeName + ".homename");
                                homeName = args[2]; //ToDo fix msg it shows args instead of the homeName
                                if (getDataConfig().isConfigurationSection("staff-homes-locations." + homeName )) {
                                    p.sendMessage(ChatColor.GREEN + "Teleporting to " + HomeName + " in the" + worldName + " " + "@: " + ChatColor.GRAY + getDataConfig().getInt("staff-homes-locations." +  homeName + ".x") + " " + getDataConfig().getInt("staff-homes-locations." + homeName + ".y") + " " + getDataConfig().getInt("staff-homes-locations." + homeName + ".z"));
                                    Location return_location = null; //TODO Added Pinch & YAW & World. Ex: world_the_nether
                                    if (t != null) {
                                        return_location = new Location(t.getWorld(), getDataConfig().getInt("staff-homes-locations." + homeName + ".x"), getDataConfig().getInt("staff-homes-locations." + homeName + ".y"), getDataConfig().getInt("staff-homes-locations." + homeName + ".z"));
                                    }
                                    if (return_location != null) {
                                        p.teleport(return_location);
                                    }
                                } else {
                                    p.sendMessage(ChatColor.DARK_RED + "That player does not have a home set.");
                                }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
                        }
                    }
                    if (args.length == 2 && args[0].equalsIgnoreCase("delete")) { //staffhome delete playerNamehomeNAme
                        homeName = args[1];
                        String HomeName = getDataConfig().getString("staff-homes-locations." + homeName + ".homename");
                        String PlayerName = getDataConfig().getString("staff-homes-locations." + homeName + ".owner");
                        if (p.hasPermission("simpleminecraftbot.staff") || p.isOp()) {
                            if (getDataConfig().isConfigurationSection("staff-homes-locations." + homeName)) {
                                    System.out.println("testing delete");
                                    getDataConfig().set("staff-homes-locations." + homeName, null); //TODO make it delete the home with the owner with it. Essentially it would find the name of the home and then find the owner lastly delete the home
                                    SaveData();
                                    p.sendMessage(ColorMsg.color("&c&lStaff Home Deleted!"));
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    private void saveLocation(@NotNull Player p) {
        Location l = p.getLocation();
        //TODO Make Variables for the cords
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("set-message") + Math.round(l.getX()) + " " + Math.round(l.getY()) + " " + Math.round(l.getZ())));
        getDataConfig().createSection("staff-homes-locations." + homeName);
        getDataConfig().set("staff-homes-locations." +  homeName + ".homename", homeName);
        getDataConfig().set("staff-homes-locations." +  homeName + ".owner",  p.getName());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".x", l.getX());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".y", l.getY());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".z", l.getZ());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".yaw", l.getYaw());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".pitch", l.getPitch());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".worldName", l.getWorld().getName());
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