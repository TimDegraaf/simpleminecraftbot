package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import games.negative.framework.message.Message;
import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class CommandCheck implements CommandExecutor {
    private final Main instance;
    public CommandCheck(Main instance){
        this.instance = instance;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1) {
            new Message("&cUsage: /staffcheck <player>").send(sender);
            return false;
        }
        new Message("&3" + args[0] + "'s playtime so far for this week: &b%playtime%.").replace("%playtime%", getPlayTime(Bukkit.getOfflinePlayer(args[0]))).send(sender);
        return true;
    }
    private @NotNull String getPlayTime(@NotNull OfflinePlayer p) {
        FileConfiguration staff = instance.getConfig();
        long time = staff.getLong(String.valueOf(p.getUniqueId()));
        return instance.convertTime(time);
    }

}