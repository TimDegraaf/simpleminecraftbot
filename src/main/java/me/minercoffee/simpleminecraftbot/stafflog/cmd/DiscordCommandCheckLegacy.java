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
import java.util.Objects;

public class DiscordCommandCheckLegacy extends ListenerAdapter {
    public DiscordCommandCheckLegacy() {
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMember() != null) {
            String roles = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
            if ((roles != null && roles.contains("staff")) || roles != null && roles.contains("Owner")) {

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
}
