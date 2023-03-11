package me.minercoffee.simpleminecraftbot.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

public class BotCommands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("sup")) {
            TextInput name = TextInput.create("sup-name", "Name", TextInputStyle.SHORT)
                    .setMinLength(1)
                    .setRequired(true)
                    .build();

            TextInput message = TextInput.create("sup-message", "Message", TextInputStyle.PARAGRAPH)
                    .setMinLength(10)
                    .setMaxLength(100)
                    .setPlaceholder("Put a cool message here")
                    .build();
            Modal modal = Modal.create("sup-modal", "Say Wassup")
                    .addActionRows(ActionRow.of(name), ActionRow.of(message))
                    .build();
            event.replyModal(modal).queue();
        }
    }
}
