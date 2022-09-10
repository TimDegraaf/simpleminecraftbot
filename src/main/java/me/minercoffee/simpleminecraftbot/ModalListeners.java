package me.minercoffee.simpleminecraftbot;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ModalListeners extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("sup-modal")){
            String name = event.getValue("sup-name").getAsString();
            String message = event.getValue("sup-message").getAsString();
            event.reply("Sup," + name + ". Message: " + message).queue();
        }
    }
}
