package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static me.minercoffee.simpleminecraftbot.Main.jda;

public class OnlineStaff extends ListenerAdapter {

    private final Main plugin;

    public OnlineStaff(Main plugin) {
        this.plugin = plugin;
    }

    private TextChannel staffchannel;
    @SuppressWarnings("deprecation")
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String staffplaytimechannel = "965844461772996628";
        staffchannel = jda.getTextChannelById(staffplaytimechannel);
        String roles = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
        if (roles.contains("staff") || roles.contains("Owner")) {
            if (e.getAuthor().isBot()) return;
            List<String> command = Arrays.asList(e.getMessage().getContentRaw().split(" "));
            if (!command.get(0).equalsIgnoreCase("-onlinestaff")) return;
            ArrayList<Player> list = new ArrayList<>(plugin.getServer().getOnlinePlayers());
            String name = command.get(1);
            OfflinePlayer p = Bukkit.getOfflinePlayer(name);
            for (Player staff : list) {
                if (staff.isOp() | staff.hasPermission("illusive.staff")) {
                    e.getChannel().sendMessage("**" + p.getName() + "** is **" + "** available .").queue();
                }
            }
            stafflonline(Bukkit.getOfflinePlayer("MinerCoffee97"), "There are staff online", true, Color.GREEN);
        }
    }

    public void stafflonline(OfflinePlayer avatar, String content, boolean contentAuthorLine, Color color) {
        if (staffchannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : avatar.getName(),
                        null,
                        "https://crafatar.com/avatars/" + avatar.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.orange);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        staffchannel.sendMessageEmbeds(builder.build()).queue();
    }
}
