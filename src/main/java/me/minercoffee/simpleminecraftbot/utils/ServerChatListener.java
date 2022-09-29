package me.minercoffee.simpleminecraftbot.utils;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class ServerChatListener implements Listener {
    private final Main plugin;
    private final Embles embles;
    public ServerChatListener(Main plugin, Embles embles){
        this.plugin = plugin;
        this.embles = embles;
    }
    String onfistjoin = DataManager.getMessagesConfig().getString("discord-srv-messages.onfirstjoin");
    String onjoin = DataManager.getMessagesConfig().getString("discord-srv-messages.onjoin");
    String onquit = DataManager.getMessagesConfig().getString("discord-srv-messages.onquit");
    String ondeath = DataManager.getMessagesConfig().getString("discord-srv-messages.ondeath");
    String onadvancement = DataManager.getMessagesConfig().getString("discord-srv-messages.onadvancement");
    @EventHandler
    public void onFirstJoin(@NotNull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPlayedBefore()) {
           embles.SendFirstJoinmsg(player, player.getName() + " " + onfistjoin);
        }
    }
    @EventHandler
    public void onChat(@NotNull AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage().toLowerCase();
        embles.SendMsg(player, e.getPlayer().getName() + " >> " + " "  + message);
    }
    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e){
        Player player = e.getPlayer();
        embles.sendMessage(player, e.getPlayer().getName() + " " + onjoin);
    }
    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent e){
        Player player = e.getPlayer();
        embles.sendoffmsg(e.getPlayer(), player.getName() + " " + onquit);
    }
    @EventHandler
    public void onDeath(@NotNull PlayerDeathEvent e){
        Player p = e.getEntity();
        String deathMessage = e.getDeathMessage() == null ? p.getName() + " " + ondeath : e.getDeathMessage();
        embles.senddeathmsg(p, deathMessage);
    }
    @EventHandler
    public void onAdvancement(@NotNull PlayerAdvancementDoneEvent e){
        Player player = e.getPlayer();
        String advancementKey  = e.getAdvancement().getKey().getKey();
        String display = plugin.advancementToDisplayMap.get(advancementKey);
        if (display == null ) return;
        embles.sendadvancementmsg(player, player.getName() + " " + onadvancement + " " + "["+ display + "]");
    }
}