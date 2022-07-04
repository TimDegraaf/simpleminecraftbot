package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class DiscordCommandCheckLegacy extends ListenerAdapter {
    public DiscordCommandCheckLegacy() {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMember() != null) {
            if (Main.getInstance().checkMemberRoles(e.getMember(), "staff") == null) return;
            if (e.getAuthor().isBot()) return;
            List<String> command = Arrays.asList(e.getMessage().getContentRaw().split(" "));
            if (!command.get(0).equalsIgnoreCase("-staffcheck")) return;
            String name = command.get(1);
            OfflinePlayer p = Bukkit.getOfflinePlayer(name);
            FileConfiguration config = Main.getInstance().getConfig();
            String time = Main.getInstance().convertTime(config.getLong(String.valueOf(p.getUniqueId())));
            e.getChannel().sendMessage("**" + p.getName() + "** has **" + time + "** logged this week.").queue();
            }
        }
    }
