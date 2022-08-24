package me.minercoffee.simpleminecraftbot.ticket;

import me.minercoffee.simpleminecraftbot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.Objects;


public class ButtonListener extends ListenerAdapter {
    Main plugin;
    public ButtonListener(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent e) {
        try {
            e.deferEdit().queue();
            if (e.getButton().getId().equals("openTicket")){
                String role = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
                if (role.contains("player")) {
                    int min = 1;
                    int max = 99999;
                    int random_int = (int) Math.floor(Math.random() + (max - min + 1) + min);
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    Date date = new Date();
                    Guild guild = e.getGuild();
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.GREEN);
                    embed.setTitle(e.getUser().getName() + " 's Ticket");
                    embed.setDescription(e.getMember().getAsMention() + "Welcome!");
                    embed.setDescription("A staff member will be with you shortly!");
                    String playerRole = "944353114138484756";
                    String Catorgory_ID = "975975706544717824";
                    if (guild != null) {
                           guild.createTextChannel("ticket-" + random_int, guild.getCategoryById(Catorgory_ID))
                                   .addPermissionOverride(e.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                                   .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                                   .addPermissionOverride(guild.getRoleById(playerRole), EnumSet.of(Permission.VIEW_CHANNEL), null)
                                   .complete().sendMessageEmbeds(embed.build()).setActionRow(closeButton(), claimButton()).queue();
                           EmbedBuilder embedTeam = new EmbedBuilder();
                           embedTeam.setColor(Color.GREEN);
                           embedTeam.setTitle("Ticket System");
                           embedTeam.addField("Person", e.getMember().getAsMention(), true);
                           embedTeam.addField("Date", format.format(date), true);
                           Objects.requireNonNull(guild.getTextChannelById(Catorgory_ID)).sendMessageEmbeds(embedTeam.build()).queue();
                        }
                    }
                if (role.contains("Owner")){
                    EmbedBuilder embed = new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Ticket System")
                            .setDescription("You have the ticket role.");
                    e.getUser().openPrivateChannel().complete().sendMessageEmbeds(embed.build()).queue();
                }
            } else if (e.getButton().getId().equals("closeButton")) {
                String roles = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
                if (roles.contains("Owner") || roles.contains("Staff")){
                    e.getInteraction().getChannel().delete().queue();
                }
            } else if (e.getButton().getId().equals("claimButton")) {
                if (Objects.requireNonNull(e.getInteraction().getMember()).hasPermission(Permission.KICK_MEMBERS)) {
                    String roles = String.valueOf(Objects.requireNonNull(e.getMember()).getRoles());
                    if (roles.contains("Owner")) return;
                    if (roles.contains("Staff")) return;
                    TextChannel channel = (TextChannel) e.getChannel().asGuildMessageChannel();
                    e.getInteraction().getMessage().delete().queue();
                    EmbedBuilder embed = new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Ticket System")
                            .setDescription(e.getInteraction().getUser().getAsMention() + " has claimed the ticket");
                    channel.sendMessageEmbeds(embed.build()).setActionRow(closeButton()).complete();
                    PermissionOverride permissionOverride = channel.getPermissionOverride(Objects.requireNonNull(e.getMember()));
                    if (permissionOverride == null) {
                        channel.upsertPermissionOverride(e.getMember()).setAllowed(Permission.VIEW_CHANNEL).queue();
                    } else {
                        permissionOverride.getManager().setAllowed(Permission.VIEW_CHANNEL);
                    }
                    PermissionOverride permissionOverride2 = channel.getPermissionOverride(Objects.requireNonNull(Objects.requireNonNull(e.getGuild()).getRoleById("975975706544717824"))); //same as guild id
                    if (permissionOverride2 == null) {
                        channel.upsertPermissionOverride(Objects.requireNonNull(e.getGuild().getRoleById("975975706544717824"))).setDenied(Permission.VIEW_CHANNEL).queue();
                    } else {
                        permissionOverride2.getManager().setDenied(Permission.VIEW_CHANNEL).queue();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Contract(" -> new")
    private @NotNull Button closeButton(){
        return Button.danger("closeButton", "Close");
    }
    @Contract(" -> new")
    private @NotNull Button claimButton(){
        return Button.success("claimButton", "Claim");
    }
}