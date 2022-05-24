package me.minercoffee.simpleminecraftbot.framework;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum FrameworkMessage {
    COMMAND_NO_PERMISSION(Collections.singletonList("&cYou do not have permission to use this command.")),
    COMMAND_DISABLED(Collections.singletonList("&cThis command is currently disabled.")),
    COMMAND_CANNOT_USE_THIS_AS_PLAYER(Collections.singletonList("&cYou cannot use this command as a player.")),
    COMMAND_CANNOT_USE_THIS_AS_CONSOLE(Collections.singletonList("&cYou cannot use this command as a console.")),
    COMMAND_USAGE(Collections.singletonList("&cUsage: &7/%command% %usage%"));

    private final List<String> defaultMessage;
    private Message message;

    public static void init() {
        Arrays.stream(values()).forEach((message) -> {
            message.message = new Message(message.defaultMessage);
        });
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void send(CommandSender sender) {
        this.message.send(sender);
    }

    public void send(Iterable<CommandSender> players) {
        this.message.send(players);
    }

    public void broadcast() {
        this.message.broadcast();
    }

    public Message replace(String o1, String o2) {
        return this.message.replace(o1, o2);
    }

    private FrameworkMessage(List defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public List<String> getDefaultMessage() {
        return this.defaultMessage;
    }

    public Message getMessage() {
        return this.message;
    }
}
