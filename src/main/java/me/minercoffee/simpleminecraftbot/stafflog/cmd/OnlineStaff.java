package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class OnlineStaff extends ListenerAdapter {
    public OnlineStaff(){
    }

    private TextChannel staffchannel;
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        List<String> command = Arrays.asList(e.getMessage().getContentRaw().split(" "));
        if (!command.get(0).equalsIgnoreCase("-onlinestaff")) return;
            System.out.println("test2");
        }

    public void stafflonline(String content, boolean contentAuthorLine, Color color, Player player) {
        if (staffchannel == null) return;
        for (Player list : Bukkit.getOnlinePlayers()) {
            if (list.hasPermission("simpleminecraftbot.staff") | list.isOp()) {
                System.out.println("test");
                list.getDisplayName();
            }

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

        staffchannel.sendMessageEmbeds(builder.build()).queue();
    }
}
}
