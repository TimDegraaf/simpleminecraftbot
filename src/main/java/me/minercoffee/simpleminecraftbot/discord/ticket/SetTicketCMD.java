package me.minercoffee.simpleminecraftbot.discord.ticket;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class SetTicketCMD extends ListenerAdapter {

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMessage().getContentStripped().equalsIgnoreCase(Main.getPREFIX() + "setTicket")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.GREEN);
            embed.setTitle("Ticket Support");
            embed.setDescription("Click on the button and get a ticket support.");
            e.getChannel().sendMessageEmbeds(embed.build()).setActionRow(Button.success("openTicket", "openTicket").withEmoji(Emoji.fromUnicode("\uD83C\uDF9F"))).queue();
        }
    }
}