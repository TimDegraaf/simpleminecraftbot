package me.minercoffee.simpleminecraftbot.framework.commands;

import me.minercoffee.simpleminecraftbot.framework.CommandInfo;
import me.minercoffee.simpleminecraftbot.framework.commands.event.SubCommandLogEvent;
import me.minercoffee.simpleminecraftbot.framework.commands.shortcommand.ShortCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public abstract class SubCommand implements CommandBase {
    private final List<SubCommand> subCommands;
    private String argument;
    private List<String> aliases;
    private String permission;
    private boolean consoleOnly;
    private boolean playerOnly;
    private boolean disabled;
    private String[] params;
    private Consumer<SubCommandLogEvent> subCommandLogEventConsumer;
    private CommandBase parent;

    public SubCommand() {
        this(null, null);
    }

    public SubCommand(String argument) {
        this(argument, Collections.emptyList());
    }

    public SubCommand(String argument, List<String> aliases) {
        this.subCommands = new ArrayList();
        this.permission = "";
        this.consoleOnly = false;
        this.playerOnly = false;
        this.argument = argument;
        this.aliases = aliases;
        if (this.getClass().isAnnotationPresent(CommandInfo.class)) {
            CommandInfo annotation = this.getClass().getAnnotation(CommandInfo.class);
            this.setArgument(annotation.name());
            List<String> a = new ArrayList(Arrays.asList(annotation.aliases()));
            if (!a.get(0).isEmpty()) {
                this.setAliases(a);
            }

            if (!annotation.permission().isEmpty()) {
                this.setPermission(annotation.permission());
            }

            List<String> shortCmds = new ArrayList(Arrays.asList(annotation.shortCommands()));
            if (!((String)shortCmds.get(0)).isEmpty()) {
                ShortCommands.getInstance().addShortSubCommand(this, annotation.shortCommands());
            }

            List<String> paramList = new ArrayList(Arrays.asList(annotation.args()));
            if (!paramList.get(0).isEmpty()) {
                this.params = annotation.args();
            }
        }

    }

    public void addSubCommands(SubCommand... subCommands) {
        this.subCommands.addAll(Arrays.asList(subCommands));
    }

    public void runSubCommand(SubCommand subCommand, CommandSender sender, String[] args) {
        subCommand.execute(sender, args);
    }

    public boolean runLogEvent(CommandBase base, CommandSender sender, String[] args) {
        if (this.subCommandLogEventConsumer == null) {
            return false;
        } else {
            SubCommandLogEvent event = new SubCommandLogEvent(sender, args, this);
            Bukkit.getPluginManager().callEvent(event);
            return event.isCancelled();
        }
    }

    public void ifHasPermission(@NotNull CommandSender sender, @NotNull String perm, @NotNull Consumer<CommandSender> consumer) {
        if (sender.hasPermission(perm)) {
            consumer.accept(sender);
        }

    }

    public void ifNotHasPermission(@NotNull CommandSender sender, @NotNull String perm, @NotNull Consumer<CommandSender> consumer) {
        if (!sender.hasPermission(perm)) {
            consumer.accept(sender);
        }

    }

    public void ifPlayer(@NotNull CommandSender sender, @NotNull Consumer<Player> consumer) {
        if (sender instanceof Player) {
            consumer.accept((Player)sender);
        }

    }

    public void ifConsole(@NotNull CommandSender sender, @NotNull Consumer<ConsoleCommandSender> consumer) {
        if (sender instanceof ConsoleCommandSender) {
            consumer.accept((ConsoleCommandSender)sender);
        }

    }

    public @Nullable CommandBase getParent() {
        return this.parent;
    }

    public void setParent(@NotNull CommandBase parent) {
        this.parent = parent;
    }

    public @NotNull String getName() {
        return this.argument;
    }

    public Consumer<SubCommandLogEvent> getSubCommandLogEvent() {
        return this.subCommandLogEventConsumer;
    }

    public String getPermission() {
        return this.permission;
    }

    public List<SubCommand> getSubCommands() {
        return this.subCommands;
    }

    public String getArgument() {
        return this.argument;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public boolean isConsoleOnly() {
        return this.consoleOnly;
    }

    public boolean isPlayerOnly() {
        return this.playerOnly;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public String[] getParams() {
        return this.params;
    }

    public Consumer<SubCommandLogEvent> getSubCommandLogEventConsumer() {
        return this.subCommandLogEventConsumer;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setConsoleOnly(boolean consoleOnly) {
        this.consoleOnly = consoleOnly;
    }

    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public void setSubCommandLogEventConsumer(Consumer<SubCommandLogEvent> subCommandLogEventConsumer) {
        this.subCommandLogEventConsumer = subCommandLogEventConsumer;
    }
}
