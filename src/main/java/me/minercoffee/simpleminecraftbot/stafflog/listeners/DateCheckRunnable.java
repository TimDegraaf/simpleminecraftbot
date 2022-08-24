package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DateCheckRunnable extends BukkitRunnable {
    private final Main plugin;
    public DateCheckRunnable(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date

        if (date.getDay() == 0 && calendar.get(Calendar.HOUR_OF_DAY) == 22) {
            // discord stuff
            StringBuilder toSend = new StringBuilder();
            try {
                for (String uuid : Main.getInstance().getConfig().getKeys(false)) {
                    toSend.append("- **").append(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).append("** has played for **").append(plugin.convertTime(Main.getInstance().getConfig().getLong(uuid))).append("** this week\n");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            System.out.println(toSend);
            plugin.sendstaffonline(Bukkit.getOfflinePlayer("MinerCoffee97"), "**WEEKLY SUMMARY**\n" + toSend, false, Color.GRAY);
            try {
                // clear the config
                for (String key : Main.getInstance().getConfig().getKeys(false)) {
                    Main.getInstance().getConfig().set(key, null);
                }
                Main.getInstance().saveConfig();
                Main.ConfigUpdater();
                plugin.saveConfig();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            try {
                cancel();
            } catch (IllegalStateException e){
                e.printStackTrace();
            }

        }
    }
}