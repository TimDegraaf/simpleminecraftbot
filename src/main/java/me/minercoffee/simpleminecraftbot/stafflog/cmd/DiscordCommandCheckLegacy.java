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

import static me.minercoffee.simpleminecraftbot.utils.DataManager.getStaffplaytimeConfig;

public class DiscordCommandCheckLegacy extends ListenerAdapter {
    private final Main plugin;
    public DiscordCommandCheckLegacy(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        if (e.getMember() != null) return;
        String roles = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
        String ownerRole = plugin.getConfig().getString("roles_owner_id");
        String staffRole = plugin.getConfig().getString("roles_staff_id");
        String prefix = Main.getPREFIX();
        if (ownerRole != null && (staffRole != null && roles.contains(staffRole) || roles != null && roles.contains(ownerRole))) {
            List<String> command = Arrays.asList(e.getMessage().getContentRaw().split(" "));
            if (!command.get(0).equalsIgnoreCase(prefix + "staffcheck")) return;
            String name = command.get(1);
            OfflinePlayer p = Bukkit.getOfflinePlayer(name);
            if (p.hasPlayedBefore()) {
                FileConfiguration config = getStaffplaytimeConfig();
                String time = Main.getInstance().convertTime(config.getLong(String.valueOf(p.getUniqueId())));
                e.getChannel().sendMessage("**" + p.getName() + "** has **" + time + "** logged this week.").queue();
            } else {
                System.out.println("The player never played on the server.");
            }
        }
    }
}
