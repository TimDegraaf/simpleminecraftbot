package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DateCheckRunnable extends BukkitRunnable {
    private final Main plugin;
    private final List<String> toSend = new ArrayList<>();

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
            for (String uuid : Main.getInstance().getConfig().getKeys(false)) {
                toSend.append("- **").append(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).append("** has played for **").append(plugin.convertTime(Main.getInstance().getConfig().getLong(uuid))).append("** this week\n");
            }
            System.out.println(toSend);
            Main.getInstance().sendstaffEmbed(Bukkit.getOfflinePlayer("MinerCoffee97"), "**WEEKLY SUMMARY**\n" + toSend, false, Color.GRAY);

            // clear the config
            for (String key : Main.getInstance().getConfig().getKeys(false)) {
                Main.getInstance().getConfig().set(key, null);
            }
            Main.getInstance().saveConfig();
        }
    }

}