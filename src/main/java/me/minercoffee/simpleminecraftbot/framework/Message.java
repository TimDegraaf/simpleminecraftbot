package me.minercoffee.simpleminecraftbot.framework;

import me.minercoffee.simpleminecraftbot.framework.utils.Utils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class Message {
    private final String initial;
    private String message;

    public Message(String... msg) {
        String actual = String.join("\n", msg);
        this.setMessage(actual);
        this.initial = actual;
    }

    public Message(@NotNull String msg) {
        this.setMessage(msg);
        this.initial = msg;
    }

    public Message(@NotNull Collection<String> msg) {
        String actual = String.join("\n", msg);
        this.setMessage(actual);
        this.initial = actual;
    }

    public Message replace(String placeholder, String replacement) {
        String newMSG = this.message;
        newMSG = newMSG.replaceAll(placeholder, replacement);
        this.setMessage(newMSG);
        return this;
    }

    public void send(@NotNull CommandSender sender) {
        if (!this.message.contains("\n")) {
            sender.sendMessage(Utils.color(this.getMessage()));
            this.setMessage(this.getInitial());
        } else {
            String[] msg = this.getMessage().split("\n");
            String[] var3 = msg;
            int var4 = msg.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String s = var3[var5];
                sender.sendMessage(Utils.color(s));
            }

            this.message = this.initial;
        }
    }

    public void send(@NotNull Iterable<CommandSender> players) {
        players.forEach(this::send);
    }

    public void broadcast() {
        if (!this.getMessage().contains("\n")) {
            Utils.broadcast(this.getMessage());
            this.setMessage(this.getInitial());
        } else {
            String[] msg = this.getMessage().split("\n");
            String[] var2 = msg;
            int var3 = msg.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String s = var2[var4];
                Utils.broadcast(s);
            }

            this.setMessage(this.getInitial());
        }
    }

    public String getInitial() {
        return this.initial;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
