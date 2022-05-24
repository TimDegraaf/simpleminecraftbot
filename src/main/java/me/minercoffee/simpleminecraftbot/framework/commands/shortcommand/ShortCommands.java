package me.minercoffee.simpleminecraftbot.framework.commands.shortcommand;


import me.minercoffee.simpleminecraftbot.framework.commands.Command;
import me.minercoffee.simpleminecraftbot.framework.commands.SubCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ShortCommands {
    private static ShortCommands instance;

    public ShortCommands() {
    }

    public abstract void addShortCommand(@NotNull Command var1, @NotNull String[] var2);

    public abstract void addShortSubCommand(@NotNull SubCommand var1, @NotNull String[] var2);

    public abstract Optional<Command> getCommand(@NotNull String var1);

    public abstract Optional<SubCommand> getSubCommand(@NotNull String var1);

    public static ShortCommands getInstance() {
        return instance;
    }

    protected static void setInstance(ShortCommands instance) {
        ShortCommands.instance = instance;
    }
}
