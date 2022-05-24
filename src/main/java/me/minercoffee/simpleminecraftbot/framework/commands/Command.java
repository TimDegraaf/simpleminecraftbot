package me.minercoffee.simpleminecraftbot.framework.commands;

import me.minercoffee.simpleminecraftbot.framework.CommandInfo;
import me.minercoffee.simpleminecraftbot.framework.commands.event.CommandLogEvent;
import me.minercoffee.simpleminecraftbot.framework.commands.shortcommand.ShortCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class Command extends org.bukkit.command.Command implements CommandBase {
    private final List<SubCommand> subCommands;
    public boolean consoleOnly;
    public boolean playerOnly;
    public boolean disabled;
    public String permissionNode;
    private String[] params;
    private TabCompleter completer;
    private Consumer<CommandLogEvent> logEvent;
    private CommandBase parent;

    public Command() {
        this("1");
    }

    public Command(@NotNull String name) {
        this(name, "", Collections.emptyList());
    }

    public Command(@NotNull String name, @NotNull String description) {
        this(name, description, Collections.emptyList());
    }

    public Command(@NotNull String name, @NotNull Collection<String> aliases) {
        this(name, "", aliases);
    }

    public Command(@NotNull String name, @NotNull String description, @NotNull Collection<String> aliases) {
        super(name, description, "/" + name, new ArrayList(aliases));
        this.subCommands = new ArrayList();
        this.consoleOnly = false;
        this.playerOnly = false;
        this.disabled = false;
        this.permissionNode = "";
        boolean hasInfo = this.getClass().isAnnotationPresent(CommandInfo.class);
        if (hasInfo) {
            CommandInfo annotation = (CommandInfo)this.getClass().getAnnotation(CommandInfo.class);
            this.setName(annotation.name());
            if (annotation.consoleOnly()) {
                this.setConsoleOnly(true);
            }

            if (annotation.playerOnly()) {
                this.setPlayerOnly(true);
            }

            if (annotation.disabled()) {
                this.setDisabled(true);
            }

            if (!annotation.description().isEmpty()) {
                this.setDescription(annotation.description());
            }

            List<String> a = new ArrayList(Arrays.asList(annotation.aliases()));
            if (!((String)a.get(0)).isEmpty()) {
                this.setAliases(a);
            }

            if (!annotation.permission().isEmpty()) {
                this.setPermissionNode(annotation.permission());
            }

            List<String> shortCmds = new ArrayList(Arrays.asList(annotation.shortCommands()));
            if (!((String)shortCmds.get(0)).isEmpty()) {
                ShortCommands.getInstance().addShortCommand(this, annotation.shortCommands());
            }

            List<String> paramList = new ArrayList(Arrays.asList(annotation.args()));
            if (!((String)paramList.get(0)).isEmpty()) {
                this.params = annotation.args();
            }
        }

    }

    public abstract void onCommand(CommandSender var1, String[] var2);

    public void onCommand(CommandSender sender, String label, String[] args) {
        this.onCommand(sender, args);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        this.execute(sender, args);
        return true;
    }

    public void runSubCommand(SubCommand subCommand, CommandSender sender, String[] args) {
        subCommand.execute(sender, args);
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

    public void addSubCommands(SubCommand... subCommands) {
        Arrays.stream(subCommands).forEach((subCommand) -> {
            subCommand.setParent(this);
        });
        this.subCommands.addAll(Arrays.asList(subCommands));
    }

    public void setTabComplete(BiFunction<CommandSender, String[], List<String>> function) {
        this.completer = (sender, command, alias, args) -> {
            return !alias.equalsIgnoreCase(this.getName()) && !this.getAliases().contains(alias.toLowerCase()) ? null : (List)function.apply(sender, args);
        };
    }

    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (this.completer != null && this.completer.onTabComplete(sender, this, alias, args) != null) {
            return this.completer.onTabComplete(sender, this, alias, args);
        } else {
            String lastWord = args[args.length - 1];
            Player senderPlayer = sender instanceof Player ? (Player)sender : null;
            ArrayList<String> matchedPlayers = new ArrayList();
            sender.getServer().getOnlinePlayers().stream().filter((player) -> {
                return senderPlayer == null || senderPlayer.canSee(player) && StringUtil.startsWithIgnoreCase(player.getName(), lastWord);
            }).forEach((player) -> {
                matchedPlayers.add(player.getName());
            });
            matchedPlayers.sort(String.CASE_INSENSITIVE_ORDER);
            return matchedPlayers;
        }
    }

    public @NotNull String getName() {
        return super.getName();
    }

    public @Nullable CommandBase getParent() {
        return this.parent;
    }

    public void setParent(@NotNull CommandBase parent) {
        this.parent = parent;
    }

    public boolean runLogEvent(CommandBase base, CommandSender sender, String[] args) {
        if (this.logEvent == null) {
            return false;
        } else {
            CommandLogEvent event = new CommandLogEvent(sender, args, this);
            Bukkit.getPluginManager().callEvent(event);
            return event.isCancelled();
        }
    }

    public String getPermission() {
        return this.getPermissionNode();
    }

    public List<SubCommand> getSubCommands() {
        return this.subCommands;
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

    public String getPermissionNode() {
        return this.permissionNode;
    }

    public String[] getParams() {
        return this.params;
    }

    public TabCompleter getCompleter() {
        return this.completer;
    }

    public Consumer<CommandLogEvent> getLogEvent() {
        return this.logEvent;
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

    public void setPermissionNode(String permissionNode) {
        this.permissionNode = permissionNode;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public void setCompleter(TabCompleter completer) {
        this.completer = completer;
    }

    public void setLogEvent(Consumer<CommandLogEvent> logEvent) {
        this.logEvent = logEvent;
    }
}
