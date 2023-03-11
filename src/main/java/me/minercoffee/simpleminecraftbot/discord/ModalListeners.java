package me.minercoffee.simpleminecraftbot.discord;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ModalListeners extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("sup-modal")){
            String name = Objects.requireNonNull(event.getValue("sup-name")).getAsString();
            String message = Objects.requireNonNull(event.getValue("sup-message")).getAsString();
            event.reply("Sup," + name + ". Message: " + message).queue();
        }
    }
}
