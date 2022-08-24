package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import me.minercoffee.simpleminecraftbot.utils.ColorMsg;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerLogListener implements Listener, TabExecutor {
    int i = 1;
    private final Main plugin;
    private final HashMap<UUID, Long> map;
    @Contract(pure = true)
    public PlayerLogListener(@NotNull Main plugin) {
        this.plugin = plugin;
        this.map = plugin.getMap();
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("illusive.staff")){
            clockIn(p.getUniqueId());
        }
    }
    @EventHandler
    public void CurrentTime(@NotNull PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("illusive.staff")) {
            if (p.hasPlayedBefore()) {
                currentTime(p);
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(@NotNull PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("illusive.staff")) {
            clockOut(p);
            saveAllPlayers();
        }
    }

    private @NotNull Long getTimeFromConfig(UUID p) {
        FileConfiguration config = Main.getInstance().getConfig();
        return config.getLong(String.valueOf(p));
    }
    public void clockIn(UUID p) {
        if (map != null) {
            map.put(p, System.currentTimeMillis());
        }
        System.out.print("clocked in " + p);
    }

    public void clockOut(@NotNull Player p) {
        long logoutTime = System.currentTimeMillis();
        long loginTime = 0;
        if (map != null) {
            loginTime = map.get(p.getUniqueId());
            map.remove(p.getUniqueId());
        }
        FileConfiguration config = Main.getInstance().getConfig();
        Long timeToday = logoutTime - loginTime;
        Long timeFromConfig = getTimeFromConfig(p.getUniqueId());
        Long toSet = timeToday + timeFromConfig;
        config.set(String.valueOf(p.getUniqueId()), toSet);
        Main.getInstance().saveConfig();
        plugin.senddtaffoffline(p, p.getName() + " logged off with " + plugin.convertTime(toSet) + " played this week.", false, Color.GRAY);
    }

    public void saveAllPlayers() {
        for (UUID p : map.keySet()) {
            long logoutTime = System.currentTimeMillis();
            long loginTime = map.get(p);
            map.remove(p);
            FileConfiguration config = Main.getInstance().getConfig();
            config.set(String.valueOf(p), (logoutTime - loginTime) + getTimeFromConfig(p));
            Main.getInstance().saveConfig();
            if (Bukkit.getPlayer(p) != null) { // check if player is online
                clockIn(p);
            }
        }
    }
    public void currentTime(Player p) {
        long logoutTime = System.currentTimeMillis();
        long loginTime;
        try {
            if (map != null) {
                loginTime = map.get(p.getUniqueId());
                FileConfiguration config = Main.getInstance().getConfig();
                Long timeToday = logoutTime - loginTime;
                Long timeFromConfig = getTimeFromConfig(p.getUniqueId());
                Long toSet = timeToday + timeFromConfig;
                config.set(String.valueOf(p.getUniqueId()), toSet);
                Main.getInstance().saveConfig();
                plugin.sendStaffCurrentTime(p, p.getName() + " has " + plugin.convertTime(toSet) + " played this week.", false, Color.GRAY);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void afkclockOut(@NotNull Player p) {
        try {
            long logoutTime = System.currentTimeMillis();
            if (p.hasPermission("illusive.staff")) return;
            long loginTime = 0;
            if (map != null) {
                loginTime = map.get(p.getUniqueId());
                map.remove(p.getUniqueId());
            }
            FileConfiguration config = Main.getInstance().getConfig();
            Long timeToday = logoutTime - loginTime;
            Long timeFromConfig = getTimeFromConfig(p.getUniqueId());
            Long toSet = timeToday + timeFromConfig;
            config.set(String.valueOf(p.getUniqueId()), toSet);
            plugin.saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AfkclockIn(UUID p) {
        try {
            if (map != null) {
                map.put(p, System.currentTimeMillis());
            }
            System.out.print("Back from feeding onions from " + p);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (args[0].equalsIgnoreCase("on")) {
                if (p.hasPermission("illusive.staff")) {
                    clockIn(p.getUniqueId());
                    p.sendMessage(ColorMsg.color("&lGet to work!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 15, 3);
                }
            }
        }
        if (sender instanceof Player p) {
            if (args[0].equalsIgnoreCase("off")) {
                if (p.hasPermission("illusive.staff")) {
                    clockOut(p);
                    p.sendMessage(ColorMsg.color("&lI'm out!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 15, 3);
                }
            }
        }
        if (sender instanceof Player p) {
            if (args[0].equalsIgnoreCase("save")) {
                if (p.hasPermission("illusive.staff")) {
                    saveAllPlayers();
                    p.playSound(p.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 18, 7);
                    p.sendMessage(ColorMsg.color("&lStaff Playtime was saved!"));
                }
            }
        }
        if (sender instanceof Player player) {
            if (args[0].equalsIgnoreCase("all")) {
                ArrayList<Player> list = new ArrayList<>(player.getServer().getOnlinePlayers());
                if (player.isOp() | player.hasPermission("illusive.staff")) {
                    for (Player staff : list) {
                        if (staff.isOp() || staff.hasPermission("illusive.staff")) {
                            player.sendMessage((ColorMsg.color("&lThere are " + staff.getName() + " available for staff duties")));
                            player.sendMessage((ColorMsg.color("&lThere are " + i + " staff online")));
                            i++;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsArgument = new ArrayList<>();
            subcommandsArgument.add("on");
            subcommandsArgument.add("off");
            subcommandsArgument.add("save");
            subcommandsArgument.add("all");
            return subcommandsArgument;
        }
        if (args.length == 2) {
            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player player : players) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        return null;
    }
}
