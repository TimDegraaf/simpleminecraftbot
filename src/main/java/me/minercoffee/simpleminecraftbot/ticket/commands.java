package me.minercoffee.simpleminecraftbot.ticket;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import java.awt.*;


public class commands extends ListenerAdapter {

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        if (e.getMember() != null) return;
        if (e.getMessage().getContentStripped().equalsIgnoreCase(Main.getPREFIX() + "setTicket")) {
            String roles = String.valueOf((e.getMember()).getRoles());
            System.out.println("test");
            //if (roles.contains("Player") || roles.contains("staff") || roles.contains("player")){
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.GREEN);
                embed.setTitle("Ticket Support");
                embed.setDescription("Click on the button and get a ticket support.");
                e.getChannel().sendMessageEmbeds(embed.build()).setActionRow(Button.success("openTicket", "openTicket").withEmoji(Emoji.fromCustom(":ticket:", Long.parseLong("ticket"),false ))).queue();
         //   }
        }
    }
}