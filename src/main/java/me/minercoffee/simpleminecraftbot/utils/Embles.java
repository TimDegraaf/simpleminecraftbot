package me.minercoffee.simpleminecraftbot.utils;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Embles {
    private int i = 1;
    private final Main plugin;
    public Embles(Main plugin){
        this.plugin = plugin;
    }
    public void sendstaffonline(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (plugin.staffplaytimechannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.GREEN);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        LocalDate ld = LocalDate.now();
        builder.setFooter(ld.getDayOfMonth() + "/" + ld.getMonthValue() + "/" + ld.getYear() + " (day/month/year)");

        plugin.staffplaytimechannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void senddtaffoffline(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (plugin.staffplaytimechannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        LocalDate ld = LocalDate.now();
        builder.setFooter(ld.getDayOfMonth() + "/" + ld.getMonthValue() + "/" + ld.getYear() + " (day/month/year)");

        plugin.staffplaytimechannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void sendStaffCurrentTime(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (plugin.staffplaytimechannel == null) return;

        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.orange);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        LocalDate ld = LocalDate.now();
        builder.setFooter(ld.getDayOfMonth() + "/" + ld.getMonthValue() + "/" + ld.getYear() + " (day/month/year)");

        plugin.staffplaytimechannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void sendStaffChatEmbled(OfflinePlayer player, String content, Color ignoredColor) {
        if (plugin.ingamestaffchatchannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.YELLOW);
        plugin.ingamestaffchatchannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void ServerRestartingOff(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if(plugin.getConfig().getBoolean("Status-enable")) return;
        EmbedBuilder builder;
        builder = new EmbedBuilder()
                .addField("Server Restart Schedule", "", true)
                .addField("Time Converter", "https://www.timeanddate.com/worldclock/converter.html", true)
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        if (!contentAuthorLine) {
            builder.setDescription(content);
            plugin.ServerStatuschannel.sendMessageEmbeds(builder.build()).queue();
            purgeMessages(plugin.ServerStatuschannel);
        }
    }

    public void ServerRestartingOn(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (plugin.getConfig().getBoolean("Status-enable")) return;
        EmbedBuilder builder;
        builder = new EmbedBuilder().addField("Server Restart Schedule", "", true)
                .addField("Time Converter", "https://www.timeanddate.com/worldclock/converter.html", true)
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.GREEN);
        if (!contentAuthorLine) {
            builder.setDescription(content);
            plugin.ServerStatuschannel.sendMessageEmbeds(builder.build()).queue();
            purgeMessages(plugin.ServerStatuschannel);
        }
    }

    public void Serverisoffline(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (plugin.chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        plugin.chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void ServerisOnline(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (plugin.chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.GREEN);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        plugin.chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void sendMessage(Player player, String content) {
        if (plugin.chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder().setAuthor(content, null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1")
                .setFooter(mcplayercounter());
        builder.setColor(java.awt.Color.GREEN);
        plugin.chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void sendadvancementmsg(Player player, String content) {
        if (plugin.chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.CYAN);
        plugin.chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void senddeathmsg(Player player, String content){
        if (plugin.chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        plugin.chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void sendoffmsg(Player player, String content) {
        if (plugin.chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        plugin.chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void SendFirstJoinmsg(Player player, String content){
        if (plugin.chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.ORANGE);
        plugin.chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void SendMsg(Player player, String content) {
        if (plugin.chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.GRAY);
        plugin.chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private @Nullable String mcplayercounter(){
        ArrayList<Player> list = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        for (Player player : list) {
            if (player.isOnline()){
                i++;
            }
        }
        return null;
    }
    public void purgeMessages (TextChannel channel) {
        MessageHistory history = new MessageHistory(channel);
        List<Message> msg;
        try {
            msg = history.retrievePast(2).complete();
         if (history.getRetrievedHistory().isEmpty()){
             return;
         }
            channel.deleteMessages(msg).queue();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
