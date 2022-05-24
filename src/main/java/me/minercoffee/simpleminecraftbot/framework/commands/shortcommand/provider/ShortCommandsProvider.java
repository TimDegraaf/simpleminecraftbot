package me.minercoffee.simpleminecraftbot.framework.commands.shortcommand.provider;

import me.minercoffee.simpleminecraftbot.framework.commands.Command;
import me.minercoffee.simpleminecraftbot.framework.commands.SubCommand;
import me.minercoffee.simpleminecraftbot.framework.commands.shortcommand.ShortCommands;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ShortCommandsProvider extends ShortCommands {
    private final HashMap<Command, ArrayList<String>> commandShortCommands = new HashMap();
    private final HashMap<SubCommand, ArrayList<String>> subCommandShortCommands = new HashMap();

    public ShortCommandsProvider() {
        setInstance(this);
    }

    public void addShortCommand(@NotNull Command command, String[] commands) {
        ArrayList<String> strings = (ArrayList)this.commandShortCommands.getOrDefault(command, new ArrayList());
        Arrays.stream(commands).filter((s) -> {
            return !strings.contains(s);
        }).forEach(strings::add);
        if (this.commandShortCommands.containsKey(command)) {
            this.commandShortCommands.replace(command, strings);
        } else {
            this.commandShortCommands.put(command, strings);
        }

    }

    public void addShortSubCommand(@NotNull SubCommand command, String[] commands) {
        ArrayList<String> strings = (ArrayList)this.subCommandShortCommands.getOrDefault(command, new ArrayList());
        Arrays.stream(commands).filter((s) -> {
            return !strings.contains(s);
        }).forEach(strings::add);
        if (this.subCommandShortCommands.containsKey(command)) {
            this.subCommandShortCommands.replace(command, strings);
        } else {
            this.subCommandShortCommands.put(command, strings);
        }

    }

    public Optional<Command> getCommand(@NotNull String cmd) {
        return this.commandShortCommands.entrySet().stream().filter((entry) -> {
            return ((ArrayList)entry.getValue()).stream().anyMatch((s) -> {
                return s.equals(cmd);
            });
        }).map(Map.Entry::getKey).findFirst();
    }

    public Optional<SubCommand> getSubCommand(@NotNull String cmd) {
        return this.subCommandShortCommands.entrySet().stream().filter((entry) -> {
            return ((ArrayList)entry.getValue()).stream().anyMatch((s) -> s.equals(cmd));
        }).map(Map.Entry::getKey).findFirst();
    }
}
