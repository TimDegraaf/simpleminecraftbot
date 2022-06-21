package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Discordhelp extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        try {
            if(e.getMember() != null) return;
            if (Main.getInstance().checkMemberRoles(Objects.requireNonNull(e.getMember()), "staff") == null) return;
            List<String> ignored = Arrays.asList("462296411141177364", "918054245184450600", "918054242026131467"); //checking roles ids
            if (ignored.contains(e.getMessage().getId())) return;
            if (e.getAuthor().isBot()) return;
            List<String> command = Arrays.asList(e.getMessage().getContentRaw().split(" "));
            if (!command.get(0).equalsIgnoreCase("-help")) return;

            EmbedBuilder eb = new EmbedBuilder()
                    .setFooter(e.getAuthor().getName(), e.getAuthor().getAvatarUrl())
                    .setTitle("Command List")
                    .setDescription(getHelpMessage());
            e.getChannel().sendMessageEmbeds(eb.build()).queue();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private StringBuilder getHelpMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("`-staffcheck` | `Returns the total playtime for the specified staff member for the current week.`\n");
        sb.append("`-ping` | `Returns the current server ping.` **(BETA)**\n");
        sb.append("`-onlinestaff` | `Returns all staff members that are on the server.` **(BETA)**");
        sb.append("`?purge` | `Clears chat` **(BETA)**");
        return sb;
    }
}