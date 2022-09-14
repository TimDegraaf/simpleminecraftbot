package me.minercoffee.simpleminecraftbot.utils;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ServerChatListener implements Listener {
    private final Main plugin;
    private final Embles embles;
    public ServerChatListener(Main plugin, Embles embles){
        this.plugin = plugin;
        this.embles = embles;
    }
    @EventHandler
    public void onFirstJoin(@NotNull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPlayedBefore()) {
           embles.SendFirstJoinmsg(player, player.getName() + " has join the game for the first time!");
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
        embles.sendMessage(player, e.getPlayer().getName() + " joined the game.");
    }
    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent e){
        Player player = e.getPlayer();
        embles.sendoffmsg(e.getPlayer(), player.getName() + " left the game.");
    }
    @EventHandler
    public void onDeath(@NotNull PlayerDeathEvent e){
        Player p = e.getEntity();
        String deathMessage = e.getDeathMessage() == null ? p.getName() + " died." : e.getDeathMessage();
        embles.senddeathmsg(p, deathMessage);
    }
    @EventHandler
    public void onAdvancement(@NotNull PlayerAdvancementDoneEvent e){
        Player player = e.getPlayer();
        String advancementKey  = e.getAdvancement().getKey().getKey();
        String display = plugin.advancementToDisplayMap.get(advancementKey);
        if (display == null ) return;
        embles.sendadvancementmsg(player, player.getName() + " has made the advancement ["+ display + "]");
    }
/*
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("item")) {
            Player player = (Player) sender;
            ItemStack item = player.getItemInUse();
            embles.SendMsg(player, player.getName() + " >> " + " " + item + " ");
        }
        return false;
    }*/
}