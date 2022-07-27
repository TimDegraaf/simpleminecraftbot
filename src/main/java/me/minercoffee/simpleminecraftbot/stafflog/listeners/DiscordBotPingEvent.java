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
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMember() != null) {
            String roles = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
            if ((roles != null && roles.contains("staff")) || roles != null && roles.contains("Owner")) {
                Message message = e.getMessage();
                List<User> users = message.getMentions().getUsers();
                if (users.contains(e.getJDA().getSelfUser())) {
                    e.getMessage().reply("Use `-help` to get a list of commands.").queue();
                }
            }
        }
    }
}