package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerSaveTask extends BukkitRunnable {
    @Override
    public void run() {
        new PlayerLogListener(Main.getInstance()).saveAllPlayers();
        System.out.println("saved all players");
    }
}