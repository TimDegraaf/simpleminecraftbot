package me.minercoffee.simpleminecraftbot.afk.command;

import me.minercoffee.simpleminecraftbot.afk.AFKManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class isAFKCommand implements CommandExecutor {

    private final AFKManager afkManager;

    public isAFKCommand(AFKManager afkManager){
        this.afkManager = afkManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (sender instanceof Player p && p.hasPermission("illusive.staff")){

            if(args.length == 0){

                if(afkManager.isAFK(p)){
                    p.sendMessage("You are currently AFK.");
                }else{
                    p.sendMessage("You are not currently AFK.");
                }

            }else{
                Player target = Bukkit.getPlayerExact(args[0]);

                if(afkManager.isAFK(target)){
                    p.sendMessage(target.getName() + " is currently AFK.");
                }else{
                    p.sendMessage(target.getName() + " is not currently AFK.");
                }

            }

        }

        return true;
    }
}