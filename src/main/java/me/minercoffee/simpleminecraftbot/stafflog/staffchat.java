package me.minercoffee.simpleminecraftbot.stafflog;

import me.minercoffee.simpleminecraftbot.Main;
import me.minercoffee.simpleminecraftbot.utils.ColorMsg;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class staffchat extends ListenerAdapter implements TabExecutor {
    private final Main plugin;
    public staffchat(@NotNull Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("sc").setExecutor(this);
        plugin.getCommand("sc").setAliases(Collections.singletonList("staffchat"));
        plugin.getCommand("sc").setAliases(Collections.singletonList("sc"));
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Only for humans!");
            return false;
        }

        if (!(p.hasPermission("illusive.staff"))) {
            return false;
        }
        String message = String.join(" ", args);
        if (args.length < 1) {
            return true;
        }
        if (message.equals("")) {
            p.sendMessage(ColorMsg.color("&4Usage&6: &7/staffchat"));
        }
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission("illusive.staff") || staff.isOp()) {
                staff.sendMessage(ColorMsg.color("&7[&c&lStaff&7] &8> " + "&4" + staff.getName() + "&6: &b" + message));
                plugin.sendStaffChatEmbled(staff, staff.getName() + " >> " + " " + message, false, Color.YELLOW);
            }
        }
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> playerNames = new ArrayList<>();
        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
        Bukkit.getServer().getOnlinePlayers().toArray(players);
        for (Player player : players) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.getChannel().equals(plugin.StaffChat)) return;
        Member member = e.getMember();
        if (member == null || member.getUser().isBot()) return;
       if (member.hasPermission()) return;
       for (Player staff : Bukkit.getOnlinePlayers()) {
           if (staff.hasPermission("illusive.staff") || staff.isOp()) {
               String message = e.getMessage().getContentDisplay();
               staff.sendMessage(ChatColor.BOLD + "<" + member.getEffectiveName() + ">" + ChatColor.GRAY + " " + message);
           }
       }
    }
}