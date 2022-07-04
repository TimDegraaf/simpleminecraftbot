package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import me.minercoffee.simpleminecraftbot.utils.ColorMsg;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.UUID;

public class PlayerLogListener implements Listener, CommandExecutor {

    private final Main plugin;
    private final HashMap<UUID, Long> map;

    public PlayerLogListener(Main plugin) {
        this.plugin = plugin;
        this.map = plugin.getMap();
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission("illusive.staff")) return;
        clockIn(p.getUniqueId());
    }

    @EventHandler
    public void onStaffCurrentTime(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission("illusive.staff")) return;
        currentTime(p);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission("illusive.staff")) return;
        clockOut(p);
    }

    private Long getTimeFromConfig(UUID p) {
        FileConfiguration config = Main.getInstance().getConfig();
        return config.getLong(String.valueOf(p));
    }

    private void clockIn(UUID p) {
        try {
            map.put(p, System.currentTimeMillis());
            System.out.print("clocked in " + p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void currentTime(Player p) {
        try {
            long logoutTime = System.currentTimeMillis();
            long loginTime = map.get(p.getUniqueId());
            map.get(p.getUniqueId());
            FileConfiguration config = Main.getInstance().getConfig();
            Long timeToday = logoutTime - loginTime;
            Long timeFromConfig = getTimeFromConfig(p.getUniqueId());
            Long toSet = timeToday + timeFromConfig;
            config.set(String.valueOf(p.getUniqueId()), toSet);
            Main.getInstance().saveConfig();

            Main.getInstance().sendStaffCurrentTime(p, p.getName() + " has " + plugin.convertTime(toSet) + " played this week.", false, Color.GRAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clockOut(Player p) {
        try {
            long logoutTime = System.currentTimeMillis();
            long loginTime = map.get(p.getUniqueId());
            map.remove(p.getUniqueId());
            FileConfiguration config = Main.getInstance().getConfig();
            Long timeToday = logoutTime - loginTime;
            Long timeFromConfig = getTimeFromConfig(p.getUniqueId());
            Long toSet = timeToday + timeFromConfig;
            config.set(String.valueOf(p.getUniqueId()), toSet);
            Main.getInstance().saveConfig();
            Main.getInstance().senddtaffoffline(p, p.getName() + " logged off with " + plugin.convertTime(toSet) + " played this week.", false, Color.GRAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        System.out.println("saved all staff playtime");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args[0].equalsIgnoreCase("on")) {
                if (p.hasPermission("illusive.staff")) {
                    clockIn(p.getUniqueId());
                    p.sendMessage(ColorMsg.color("&lGet to work!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 15, 3);
                }
            }
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args[0].equalsIgnoreCase("off")) {
                if (p.hasPermission("illusive.staff")) {
                    clockOut(p);
                    p.sendMessage(ColorMsg.color("&lI'm out!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 15, 3);
                }
            }
        }
        return true;
    }
}
