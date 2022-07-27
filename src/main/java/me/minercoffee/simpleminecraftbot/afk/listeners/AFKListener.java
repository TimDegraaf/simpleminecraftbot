package me.minercoffee.simpleminecraftbot.afk.listeners;

import me.minercoffee.simpleminecraftbot.afk.AFKManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AFKListener implements Listener {

    private final AFKManager afkManager;

    public AFKListener(AFKManager afkManager) {
        this.afkManager = afkManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("illusive.staff")) {
            afkManager.playerJoined(e.getPlayer());
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("illusive.staff")) {
            afkManager.playerLeft(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("illusive.staff")) {
            afkManager.playerMoved(e.getPlayer());
        }
    }
}