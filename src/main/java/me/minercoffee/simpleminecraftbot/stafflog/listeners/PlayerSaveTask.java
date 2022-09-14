package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerSaveTask extends BukkitRunnable {

    public PlayerSaveTask(){

    }
    @Override
    public void run() {
        try {
            new PlayerLogListener(Main.instance, Main.instance.embles).saveAllPlayers();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}