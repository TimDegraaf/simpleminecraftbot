package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLogListener implements Listener {

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
        map.put(p, System.currentTimeMillis());
        System.out.print("clocked in " + p);
    }
    private void currentTime(Player p){
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
    }

    private void clockOut(Player p) {
        long logoutTime = System.currentTimeMillis();
        long loginTime = map.get(p.getUniqueId());
        map.remove(p.getUniqueId());

        FileConfiguration config = Main.getInstance().getConfig();
        Long timeToday = logoutTime - loginTime;
        Long timeFromConfig = getTimeFromConfig(p.getUniqueId());
        Long toSet = timeToday + timeFromConfig;
        config.set(String.valueOf(p.getUniqueId()), toSet);
        Main.getInstance().saveConfig();

        Main.getInstance().sendstaffEmbedoffline(p, p.getName() + " logged off with " + plugin.convertTime(toSet) + " played this week.", false, Color.GRAY);
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
}