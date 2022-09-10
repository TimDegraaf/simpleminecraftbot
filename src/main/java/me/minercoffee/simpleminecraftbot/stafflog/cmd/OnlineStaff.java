package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.Main;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnlineStaff extends ListenerAdapter {
    private final Main plugin;
    int i = 1;
    public OnlineStaff(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String ownerRole = plugin.getConfig().getString("roles_owner_id");
        String staffRole = plugin.getConfig().getString("roles_staff_id");
        if (e.getAuthor().isBot()) return;
        if (e.getMember() != null) return;
        String roles = String.valueOf((e.getMember()).getRoles());
        if (ownerRole != null && (staffRole != null && roles.contains(staffRole) || roles != null && roles.contains(ownerRole))) {
            List<String> command = Arrays.asList(e.getMessage().getContentRaw().split(" "));
            if (!command.get(0).equalsIgnoreCase("-onlinestaff")) return;
            ArrayList<Player> list = new ArrayList<>(plugin.getServer().getOnlinePlayers());
            try {
                for (Player staff : list) {
                    if (staff.isOp() || staff.hasPermission("illusive.staff")) {
                        if (staff.isOnline()) {
                            e.getChannel().sendMessage("There are" + " " + i + " staff members online.").queue();
                            i++;
                        }
                    }
                }
            } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
            }
        }
    }
}
