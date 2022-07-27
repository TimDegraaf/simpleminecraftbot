package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class Discordhelp extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        if (e.getMember() != null) return;
        String roles = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
        String[] args = e.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Main.getPREFIX() + "help")) {
            if (roles != null) return;
            if (roles.contains("staff") | roles.contains("Owner")) {
                EmbedBuilder eb = new EmbedBuilder()
                        .setFooter(e.getAuthor().getName(), e.getAuthor().getAvatarUrl())
                        .setTitle("Command List")
                        .setDescription(getHelpMessage());
                e.getChannel().sendMessageEmbeds(eb.build()).queue();
            }
        }
    }

    private @NotNull StringBuilder getHelpMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("`-staffcheck` | `Returns the total playtime for the specified staff member for the current week.`\n");
        sb.append("`-ping` | `Returns the current server ping.` **(BETA)**\n");
        sb.append("`-onlinestaff` | `Returns all staff members that are on the server.` **(BETA)**");
        sb.append("`?purge` | `Clears chat` **(BETA)**");
        return sb;
    }
}