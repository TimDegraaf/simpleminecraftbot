package me.minercoffee.simpleminecraftbot.framework.commands;

import me.minercoffee.simpleminecraftbot.framework.FrameworkMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public interface CommandBase {
    void onCommand(CommandSender var1, String[] var2);

    void runSubCommand(SubCommand var1, CommandSender var2, String[] var3);

    void ifHasPermission(@NotNull CommandSender var1, @NotNull String var2, @NotNull Consumer<CommandSender> var3);

    void ifNotHasPermission(@NotNull CommandSender var1, @NotNull String var2, @NotNull Consumer<CommandSender> var3);

    void ifPlayer(@NotNull CommandSender var1, @NotNull Consumer<Player> var2);

    void ifConsole(@NotNull CommandSender var1, @NotNull Consumer<ConsoleCommandSender> var2);

    default @Nullable Player getPlayer(String name) {
        return Bukkit.getPlayer(name);
    }

    void addSubCommands(SubCommand... var1);

    @Nullable CommandBase getParent();

    void setParent(@NotNull CommandBase var1);

    @NotNull String getName();

    boolean isDisabled();

    boolean isPlayerOnly();

    boolean isConsoleOnly();

    @Nullable String getPermission();

    String[] getParams();

    @NotNull List<SubCommand> getSubCommands();

    default void execute(CommandSender sender, String[] args) {
        boolean cancelled;
        if (this.isDisabled()) {
            cancelled = this.runLogEvent(this, sender, args);
            if (!cancelled) {
                FrameworkMessage.COMMAND_DISABLED.send(sender);
            }
        } else if (this.isPlayerOnly() && !(sender instanceof Player)) {
            cancelled = this.runLogEvent(this, sender, args);
            if (!cancelled) {
                FrameworkMessage.COMMAND_CANNOT_USE_THIS_AS_CONSOLE.send(sender);
            }
        } else if (this.isConsoleOnly() && sender instanceof Player) {
            cancelled = this.runLogEvent(this, sender, args);
            if (!cancelled) {
                FrameworkMessage.COMMAND_CANNOT_USE_THIS_AS_PLAYER.send(sender);
            }
        } else if (this.getPermission() != null && !this.getPermission().isEmpty() && !sender.hasPermission(this.getPermission())) {
            cancelled = this.runLogEvent(this, sender, args);
            if (!cancelled) {
                FrameworkMessage.COMMAND_NO_PERMISSION.send(sender);
            }
        } else {
            List<SubCommand> subCommands = this.getSubCommands();
            String[] params = this.getParams();
            if (args.length != 0 && !subCommands.isEmpty()) {
                String arg = args[0];
                String[] newArgs = (String[]) Arrays.copyOfRange(args, 1, args.length);
                Optional<SubCommand> command = subCommands.stream().filter((subCmd) -> {
                    if (subCmd.getArgument().equalsIgnoreCase(arg)) {
                        return true;
                    } else {
                        List<String> aliases = subCmd.getAliases();
                        return aliases != null && !aliases.isEmpty() ? aliases.contains(arg.toLowerCase()) : false;
                    }
                }).findFirst();
                if (command.isPresent()) {
                    this.runSubCommand((SubCommand)command.get(), sender, newArgs);
                } else {
                    cancelled = this.runLogEvent(this, sender, args);
                    if (cancelled) {
                        return;
                    }

                    if (params != null && args.length < params.length) {
                        this.sendParamMessage(params, sender);
                        return;
                    }

                    this.onCommand(sender, args);
                }

            } else {
                cancelled = this.runLogEvent(this, sender, args);
                if (!cancelled) {
                    if (params != null && args.length < params.length) {
                        this.sendParamMessage(params, sender);
                    } else {
                        this.onCommand(sender, args);
                    }
                }
            }
        }
    }

    default void sendParamMessage(String[] params, CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        String[] var4 = params;
        int var5 = params.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String param = var4[var6];
            builder.append("<").append(param).append(">").append(" ");
        }

        ArrayList<String> parentNames = new ArrayList();
        parentNames.add(this.getName());

        for(CommandBase search = this; search.getParent() != null; search = search.getParent()) {
            parentNames.add(search.getParent().getName());
        }

        Collections.reverse(parentNames);
        StringBuilder parentBuilder = new StringBuilder();
        int iteration = 0;

        for(Iterator var8 = parentNames.iterator(); var8.hasNext(); ++iteration) {
            String parentName = (String)var8.next();
            if (iteration != 0) {
                parentBuilder.append(" ");
            }

            parentBuilder.append(parentName);
        }

        FrameworkMessage.COMMAND_USAGE.replace("%command%", parentBuilder.toString()).replace("%usage%", builder.toString()).send(sender);
    }

    boolean runLogEvent(CommandBase var1, CommandSender var2, String[] var3);
}
