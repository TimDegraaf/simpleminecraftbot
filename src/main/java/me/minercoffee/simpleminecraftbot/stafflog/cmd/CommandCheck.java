package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.Main;
import me.minercoffee.simpleminecraftbot.utils.ColorMsg;
import me.minercoffee.simpleminecraftbot.utils.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.minercoffee.simpleminecraftbot.utils.DataManager.*;

public class CommandCheck implements CommandExecutor {
    private final Main instance;
    public CommandCheck(Main instance){
        this.instance = instance;
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(ColorMsg.color("&cUsage: /staffcheck <player>"));
            return false;
        }
        player.sendMessage(ColorMsg.color("&3" + args[0] + "'s playtime so far for this week: &b%playtime%.").replace("%playtime%", getPlayTime(Bukkit.getOfflinePlayer(args[0]))));
        return true;
    }
    private @NotNull String getPlayTime(@NotNull OfflinePlayer p) {
        FileConfiguration staff = getStaffplaytimeConfig();
        long time = staff.getLong(String.valueOf(p.getUniqueId()));
        return instance.convertTime(time);
    }
}