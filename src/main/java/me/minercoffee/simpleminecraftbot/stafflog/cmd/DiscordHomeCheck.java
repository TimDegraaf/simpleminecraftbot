package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static me.minercoffee.simpleminecraftbot.utils.DataManager.getDataConfig;
public class DiscordHomeCheck extends ListenerAdapter {
    private final Main plugin;
    public DiscordHomeCheck(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        if (e.getMember() != null) return;
        String roles = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
        String ownerRole = plugin.getConfig().getString("roles_owner_id");
        String staffRole = plugin.getConfig().getString("roles_staff_id");
        FileConfiguration config = getDataConfig();
        String prefix = Main.getPREFIX();
        if (ownerRole != null && (staffRole != null && roles.contains(staffRole) || roles != null && roles.contains(ownerRole))) {
            List<String> command = Arrays.asList(e.getMessage().getContentRaw().split(" "));
            if (!command.get(0).equalsIgnoreCase( prefix + "homecheck")) return;
            String home = command.get(1);
            String homeName = config.getString("staff-homes-locations." + home + ".homename");
            int current_home_x = config.getInt("staff-homes-locations." + homeName + ".x");
            int current_home_y = config.getInt("staff-homes-locations." + homeName + ".y");
            int current_home_z = config.getInt("staff-homes-locations." + homeName + ".z");
            e.getChannel().sendMessage("Your homes are: " + homeName + " " + Math.round(current_home_x) + " " + Math.round(current_home_y) + " " + Math.round(current_home_z)).queue();
        }
    }
}