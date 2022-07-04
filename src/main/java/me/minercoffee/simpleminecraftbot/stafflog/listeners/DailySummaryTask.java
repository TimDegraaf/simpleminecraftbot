package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class DailySummaryTask extends BukkitRunnable {

    private final Main plugin;

    public DailySummaryTask(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        try {
            // discord stuff
            StringBuilder toSend = new StringBuilder();
            for (String uuid : Main.getInstance().getConfig().getKeys(false)) {
                toSend.append("- **").append(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).append("** has played for **").append(plugin.convertTime(Main.getInstance().getConfig().getLong(uuid))).append("** this week\n");
            }
            System.out.println(toSend);
            Main.getInstance().sendstaffonline(Bukkit.getOfflinePlayer("MinerCoffee97"), "**DAILY SUMMARY**\n" + toSend, false, Color.GRAY);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}