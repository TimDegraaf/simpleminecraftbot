package me.minercoffee.simpleminecraftbot.clearcmd;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
public class Clear extends ListenerAdapter {
    private final Main plugin;

    public Clear(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        try {
            if (e.getAuthor().isBot()) return;
            if (e.getMember() != null) return;
            String[] args = e.getMessage().getContentRaw().split(" ");
            String ownerRole = plugin.getConfig().getString("roles_owner_id");
            String staffRole = plugin.getConfig().getString("roles_staff_id");
            TextChannel commands = plugin.commandschannel;
            String role = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
            if (ownerRole != null && (staffRole != null && role.contains(staffRole) || role != null && role.contains(ownerRole))) {
                if (args[0].equalsIgnoreCase(Main.getPREFIX() + "clear")) {
                    if (args.length <= 2) {
                        sendErrorMessage(e.getChannel().asTextChannel(), e.getMember());
                    } else {
                        e.getMessage().delete().queue();
                        TextChannel target = (TextChannel) e.getMessage().getMentions().getChannels().get(0);
                        purgeMessages(target, Integer.parseInt(args[2]));
                        if (args.length > 3) {
                            StringBuilder reason = new StringBuilder();
                            for (int i = 3; i < args.length; i++) {
                                reason.append(args[i]).append(" ");
                            }
                            log(e.getMember(), args[2], reason.toString(), commands, target); //channel which the cmd can be executed.
                        } else {
                            log(e.getMember(), args[2], " ", commands, target);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void sendErrorMessage(TextChannel channel, Member member) {
        try {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Invalid Usage!");
            builder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
            builder.setColor(Color.BLACK);
            builder.setDescription(" {} = Required, [] = Optional");
            builder.addField("Proper usage: !clear {channel} {num} [reason]", "", false);
            channel.sendMessageEmbeds(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    public void log(Member clear, String num, String reason, TextChannel incident, TextChannel cleared) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Clear Report");
            builder.setColor(Color.RED);
            builder.addField("Clear Channel", cleared.getAsMention(), false);
            builder.addField("Number of Messages Cleared", num, false);
            builder.addField("Clearer", clear.getAsMention(), false);
            builder.addField("Reason", reason, false);
            builder.addField("Date", sdf.format(date), false);
            builder.addField("Time", stf.format(date), false);
            incident.sendMessageEmbeds(builder.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void purgeMessages(TextChannel channel, int num) {
        try {
            MessageHistory history = new MessageHistory(channel);
            List<Message> msg;
            msg = history.retrievePast(num).complete();
            channel.deleteMessages(msg).queue();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}