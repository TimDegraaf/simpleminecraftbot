package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.Main;
import me.minercoffee.simpleminecraftbot.utils.ColorMsg;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.minercoffee.simpleminecraftbot.stafflog.cmd.HomeUtils.MainMenuGui;
import static me.minercoffee.simpleminecraftbot.utils.DataManager.*;

public class Homes implements TabExecutor, Listener {
    public static String homeName;

    public Homes () {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (command.getName().equalsIgnoreCase("staffhome")) {
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
                if (args.length == 2 && args[0].equalsIgnoreCase("return")) {
                    homeName = args[1];
                    if (getDataConfig().isConfigurationSection("staff-homes-locations." + homeName)) {
                        Location return_location = new Location(p.getWorld(), getDataConfig().getInt("staff-homes-locations." + homeName + ".x"), getDataConfig().getInt("staff-homes-locations." + homeName + ".y"), getDataConfig().getInt("staff-homes-locations." + homeName + ".z"));
                        p.teleport(return_location);
                        p.sendMessage(ColorMsg.color(getMessagesConfig().getString("staffhome.return-message")));
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "You never set a home.");
                    }
                }

                if (args.length == 3 && args[0].equalsIgnoreCase("return")) { //staffhome return <target> homeName
                    if (p.hasPermission("simpleminecraftbot.staff") || p.isOp()) {
                        Player t = Bukkit.getPlayer(args[1]);
                        homeName = args[2];
                        if (getDataConfig().isConfigurationSection("staff-homes-locations." + homeName)) {
                            if (t != null) {
                                p.sendMessage(ChatColor.GREEN + "Teleporting to " + getDataConfig().getString("staff-homes-locations." + homeName + ".homename") + " in the " + getDataConfig().getString("staff-homes-locations." + homeName + ".worldName") + " " + "@: " + ChatColor.GRAY + getDataConfig().getInt("staff-homes-locations." + homeName + ".x") + " " + getDataConfig().getInt("staff-homes-locations." + homeName + ".y") + " " + getDataConfig().getInt("staff-homes-locations." + homeName + ".z") + " " + getDataConfig().getInt("staff-homes-locations." + homeName + ".pinch") + " " + getDataConfig().getInt("staff-homes-locations." + homeName + ".yaw"));
                                Location return_location = new Location(t.getWorld(), getDataConfig().getInt("staff-homes-locations." + homeName + ".x"), getDataConfig().getInt("staff-homes-locations." + homeName + ".y"), getDataConfig().getInt("staff-homes-locations." + homeName + ".z"));
                                p.teleport(return_location);
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "That player does not have a home set.");
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
                    }
                }

                if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7==&a&lStaff&eHomes&7 by MinerCoffee=="));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome set <name> &7- &9Set a Temporary Home"));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome return <name> &7- &9Return to Home"));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o/staffhome delete <name> &7- &9Removes your home"));
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
                if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                    return true;
                }

                if (args.length == 1 && args[0].equalsIgnoreCase("delete")) {
                    p.sendMessage(ColorMsg.color("&cPlease provide a your" + " " + getDataConfig().getString("staff-homes-locations." + homeName + ".homename") + " " + "that you created."));
                    return true;
                }


                if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
                    homeName = args[1];
                    PersistentDataContainer data = p.getPersistentDataContainer();
                    initializingStaffhomes(p);
                    int HomeCount = data.getOrDefault(new NamespacedKey(Main.getInstance(), "Staffhomes.limit"), PersistentDataType.INTEGER, 0);
                    if (!checkPlayerperms(p)){
                        if (HomeCount == 0) {
                            if (getDataConfig().isConfigurationSection("staff-homes-locations." + homeName)) {
                                if (getDataConfig().getString("staff-homes-locations." + homeName + ".owner").equals(p.getName()) && (getDataConfig().getString("staff-homes-locations." + homeName + ".homename")).equals(homeName)) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("staffhome.override-message") + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".x")) + " " + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".y")) + " " + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".z"))));
                                    saveplayerLocation(p);
                                }
                            } else if (data.has(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER)) {
                                data.set(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER, 1);
                                saveplayerLocation(p);
                            }
                        }
                        if (HomeCount == 1) {
                            if (getDataConfig().isConfigurationSection("staff-homes-locations." + homeName)) {
                                if (getDataConfig().getString("staff-homes-locations." + homeName + ".owner").equals(p.getName()) && (getDataConfig().getString("staff-homes-locations." + homeName + ".homename")).equals(homeName)) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("staffhome.override-message") + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".x")) + " " + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".y")) + " " + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".z"))));
                                    updateLocation(p);
                                }
                            } else {
                                p.sendMessage(ColorMsg.color("&cYou can't create anymore homes."));
                            }
                        }
                    } else if (HomeCount == 0) {
                            if (getDataConfig().isConfigurationSection("staff-homes-locations." + homeName)) {
                                if (getDataConfig().getString("staff-homes-locations." + homeName + ".owner").equals(p.getName()) && (getDataConfig().getString("staff-homes-locations." + homeName + ".homename")).equals(homeName)) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("staffhome.override-message") + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".x")) + " " + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".y")) + " " + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".z"))));
                                    savestaffLocation(p);
                                }
                            } else if (data.has(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER)) {
                                data.set(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER, 1);
                                savestaffLocation(p);
                            }
                        } else if (HomeCount == 1) {
                            if (getDataConfig().isConfigurationSection("staff-homes-locations." + homeName)) {
                                if (getDataConfig().getString("staff-homes-locations." + homeName + ".owner").equals(p.getName()) && (getDataConfig().getString("staff-homes-locations." + homeName + ".homename")).equals(homeName)) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("staffhome.override-message") + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".x")) + " " + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".y")) + " " + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".z"))));
                                    updateLocation(p);
                                }
                            } else if (data.has(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER)) {
                                data.set(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER, 2);
                                savestaffLocation(p);
                            }
                        } else if (HomeCount == 2) {
                            if (getDataConfig().isConfigurationSection("staff-homes-locations." + homeName)) {
                                if (getDataConfig().getString("staff-homes-locations." + homeName + ".owner").equals(p.getName()) && (getDataConfig().getString("staff-homes-locations." + homeName + ".homename")).equals(homeName)) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("staffhome.override-message") + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".x")) + " " + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".y")) + " " + Math.round(getDataConfig().getInt("staff-homes-locations." + homeName + ".z"))));
                                    updateLocation(p);
                                }
                            } else {
                                p.sendMessage(ColorMsg.color("&cYou can't create anymore homes."));
                            }
                        }
                    }
                }
            if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
                homeName = args[1];
                if (getDataConfig().isConfigurationSection("staff-homes-locations." + homeName)) {
                    if (getDataConfig().getString("staff-homes-locations." + homeName + ".owner").equals(p.getName()) && (getDataConfig().getString("staff-homes-locations." + homeName + ".homename")).equals(homeName)) {
                        RemoveLStaffHomesimits(p);
                        getDataConfig().set("staff-homes-locations." + homeName, null);
                        SaveData();
                        p.sendMessage(ColorMsg.color(messagesConfig.getString("staffhome.delete-message")));
                        UpdatingStaffLimits(p);
                    }
                } else {
                    p.sendMessage(ColorMsg.color("&cNo home found. &r&7Please try again."));
                }
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("list")) {
                PersistentDataContainer data = p.getPersistentDataContainer();
                String gethomeName = data.getOrDefault(new NamespacedKey(Main.getInstance(), "Staffhomes.limit"), PersistentDataType.STRING, homeName);

                int current_home_x = getDataConfig().getInt("staff-homes-locations." + homeName + ".x");
                int current_home_y = getDataConfig().getInt("staff-homes-locations." + homeName + ".y");
                int current_home_z = getDataConfig().getInt("staff-homes-locations." + homeName + ".z");
                if (getDataConfig().isConfigurationSection("staff-homes-locations." + homeName)) {
                    if (getDataConfig().getString("staff-homes-locations." + homeName + ".owner").equals(p.getName()) && (getDataConfig().getString("staff-homes-locations." + homeName + ".homename")).equals(homeName)) {
                        p.sendMessage(ColorMsg.color(getMessagesConfig().getString("list-message") + getDataConfig().getString("staff-homes-locations." + gethomeName + ".homename") + " " + Math.round(current_home_x) + " " + Math.round(current_home_y) + " " + Math.round(current_home_z)));

                    } else {
                            p.sendMessage(ColorMsg.color("&cNo home found. &r&7Please try again."));
                    }
                }
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("gui")) {
                MainMenuGui(p);
            }
        }
        return true;
    }
    private boolean checkPlayerperms(@NotNull Player p) {
        return p.hasPermission("simpleminecraftbot.staff") || p.isOp();
    }
    private void savestaffLocation(@NotNull Player p) {
        PersistentDataContainer data = p.getPersistentDataContainer();
        int staffhomesCount = data.getOrDefault(new NamespacedKey(Main.getInstance(), "Staffhomes.limit"), PersistentDataType.INTEGER, 0);
        Location l = p.getLocation();
        p.sendMessage(ColorMsg.color(getMessagesConfig().getString("staffhome.staff-message") + Math.round(l.getX()) + " " + Math.round(l.getY()) + " " + Math.round(l.getZ())));
        getDataConfig().createSection("staff-homes-locations." + homeName);
        getDataConfig().set("staff-homes-locations." +  homeName + ".homename", homeName);
        getDataConfig().set("staff-homes-locations." +  homeName + ".owner",  p.getName());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".x", l.getX());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".y", l.getY());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".z", l.getZ());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".yaw", l.getYaw());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".pitch", l.getPitch());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".worldName", l.getWorld().getName());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".limits", staffhomesCount);
        SaveData();
    }
    private void saveplayerLocation(@NotNull Player p) {
        PersistentDataContainer data = p.getPersistentDataContainer();
        int staffhomesCount = data.getOrDefault(new NamespacedKey(Main.getInstance(), "Staffhomes.limit"), PersistentDataType.INTEGER, 0);
        Location l = p.getLocation();
        p.sendMessage(ColorMsg.color(getMessagesConfig().getString("staffhome.player-message") + Math.round(l.getX()) + " " + Math.round(l.getY()) + " " + Math.round(l.getZ())));
        getDataConfig().createSection("staff-homes-locations." + homeName);
        getDataConfig().set("staff-homes-locations." +  homeName + ".homename", homeName);
        getDataConfig().set("staff-homes-locations." +  homeName + ".owner",  p.getName());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".x", l.getX());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".y", l.getY());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".z", l.getZ());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".yaw", l.getYaw());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".pitch", l.getPitch());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".worldName", l.getWorld().getName());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".limits", staffhomesCount);
        SaveData();
    }

    private void updateLocation(@NotNull Player p) {
        PersistentDataContainer data = p.getPersistentDataContainer();
        int staffhomesCount = data.getOrDefault(new NamespacedKey(Main.getInstance(), "Staffhomes.limit"), PersistentDataType.INTEGER, 0);
        Location l = p.getLocation();
        p.sendMessage(ColorMsg.color(getMessagesConfig().getString("staffhome.update-home-message") + Math.round(l.getX()) + " " + Math.round(l.getY()) + " " + Math.round(l.getZ())));
        getDataConfig().createSection("staff-homes-locations." + homeName);
        getDataConfig().set("staff-homes-locations." +  homeName + ".homename", homeName);
        getDataConfig().set("staff-homes-locations." +  homeName + ".owner",  p.getName());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".x", l.getX());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".y", l.getY());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".z", l.getZ());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".yaw", l.getYaw());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".pitch", l.getPitch());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".worldName", l.getWorld().getName());
        getDataConfig().set("staff-homes-locations." +  homeName +  ".limits", staffhomesCount);
        SaveData();
    }
    private void initializingStaffhomes(@NotNull Player p) {
        PersistentDataContainer data = p.getPersistentDataContainer();
        if (!data.has(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER)) {
            data.set(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER, 0);
        }
    }
    private void UpdatingStaffLimits(@NotNull Player p) {
        PersistentDataContainer data = p.getPersistentDataContainer();
        if (p.hasPermission("simpleminecraftbot.staff") || p.isOp() && data.has(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER)) {
            data.set(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER, 0);
        } else if (data.has(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER)){
            data.set(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER, 0);
        }
    }
    private void RemoveLStaffHomesimits(@NotNull Player p){
        PersistentDataContainer data = p.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"), PersistentDataType.INTEGER)){
            data.remove(new NamespacedKey(Main.getInstance(), "StaffHomes.limit"));
        }
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String  [] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsArguements = new ArrayList<>();
            subcommandsArguements.add("set");
            subcommandsArguements.add("list");
            subcommandsArguements.add("return");
            subcommandsArguements.add("delete");
            subcommandsArguements.add("help");
            subcommandsArguements.add("gui");
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