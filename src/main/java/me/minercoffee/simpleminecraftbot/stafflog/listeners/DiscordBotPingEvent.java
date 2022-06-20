package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class DiscordBotPingEvent extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMember() != null) {
            if (Main.getInstance().checkMemberRoles(e.getMember(), "staff") == null) return;
            Message message = e.getMessage();
            List<User> users = message.getMentions().getUsers();
            if (users.contains(e.getJDA().getSelfUser())) {
                e.getMessage().reply("Use `-help` to get a list of commands.").queue();
            }
        }
    }
}