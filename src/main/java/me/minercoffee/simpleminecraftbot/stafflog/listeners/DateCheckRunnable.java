package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import me.minercoffee.simpleminecraftbot.utils.Embles;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;

import static me.minercoffee.simpleminecraftbot.utils.DataManager.getStaffplaytimeConfig;
import static me.minercoffee.simpleminecraftbot.utils.DataManager.savestaffplaytime;

public class DateCheckRunnable extends BukkitRunnable {
    private final Main plugin;
    private final Embles embles;
    public DateCheckRunnable(Main plugin, Embles embles) {
        this.plugin = plugin;
        this.embles = embles;
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
                for (String uuid : getStaffplaytimeConfig().getKeys(false)) {
                    toSend.append("- **").append(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).append("** has played for **").append(plugin.convertTime(getStaffplaytimeConfig().getLong(uuid))).append("** this week\n");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            System.out.println(toSend);
            embles.sendstaffonline(Bukkit.getOfflinePlayer("MinerCoffee97"), "**WEEKLY SUMMARY**\n" + toSend, false, Color.GRAY);
            try {
                for (String key : getStaffplaytimeConfig().getKeys(false)) {
                    getStaffplaytimeConfig().set(key, null);
                    savestaffplaytime();
                }
                savestaffplaytime();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();


            }
        }
    }
}