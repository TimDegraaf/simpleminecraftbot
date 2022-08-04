package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.Main;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OnlineStaff extends ListenerAdapter {

    private final Main plugin;
    int i = 1;

    public OnlineStaff(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings({"java.lang.NullPointerException", "null"})

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        try {
            if (e.getMember() != null) return;
            String roles = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
            if (roles != null) {
                if (roles.contains("staff") || roles.contains("Owner")) {
                    if (e.getAuthor().isBot()) return;
                    List<String> command = Arrays.asList(e.getMessage().getContentRaw().split(" "));
                    if (!command.get(0).equalsIgnoreCase("-onlinestaff")) return;
                    ArrayList<Player> list = new ArrayList<>(plugin.getServer().getOnlinePlayers());
                    String name = command.get(1);
                    OfflinePlayer p = Bukkit.getOfflinePlayer(name);
                    try {
                        for (Player staff : list) {
                            if (staff.isOp() || staff.hasPermission("illusive.staff")) {
                                e.getChannel().sendMessage("There are" + " " + i + " online" + " and " + "**" + p.getName() + "** is **" + "** available/").queue();
                                i++;
                            }
                        }
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }catch(Exception var){
            var.printStackTrace();
        }
    }
}
