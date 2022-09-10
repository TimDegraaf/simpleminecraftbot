package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class DiscordBotPingEvent extends ListenerAdapter {
    private final Main plugin;
    public DiscordBotPingEvent(Main plugin){
        this.plugin = plugin;
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String ownerRole = plugin.getConfig().getString("roles_owner_id");
        String staffRole = plugin.getConfig().getString("roles_staff_id");
        if (e.getMember() != null) {
            String roles = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
            if (ownerRole != null && (staffRole != null && roles.contains(staffRole) || roles != null && roles.contains(ownerRole))) {
                Message message = e.getMessage();
                List<User> users = message.getMentions().getUsers();
                if (users.contains(e.getJDA().getSelfUser())) {
                    e.getMessage().reply("Use `-help` to get a list of commands.").queue();
                }
            }
        }
    }
}