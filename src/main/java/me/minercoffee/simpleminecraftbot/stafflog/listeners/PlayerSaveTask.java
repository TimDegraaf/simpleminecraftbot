package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import me.minercoffee.simpleminecraftbot.afk.AFKManager;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerSaveTask extends BukkitRunnable {
    public AFKManager afkManager;

    public PlayerSaveTask(){
    }
    @Override
    public void run() {
        try {
            new PlayerLogListener(afkManager, Main.instance).saveAllPlayers();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}