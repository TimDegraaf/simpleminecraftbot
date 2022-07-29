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
        afkManager.playerJoined(p);
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        afkManager.playerLeft(p);

    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        afkManager.playerMoved(p);
    }
}