package me.minercoffee.simpleminecraftbot.stafflog.ontask;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class AFKListener implements Listener {

    private final AFKManager afkManager;

    public AFKListener(AFKManager afkManager) {
        this.afkManager = afkManager;
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("illusive.staff")) {
            afkManager.playerJoined(p);
        }
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("illusive.staff")) {
            afkManager.playerLeft(p);
        }
    }

    @EventHandler
    public void onPlayerMovement(@NotNull PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("illusive.staff")) {
            afkManager.playerMoved(p);
            afkManager.checkPlayerAFKStatus(p);
        }
    }
}