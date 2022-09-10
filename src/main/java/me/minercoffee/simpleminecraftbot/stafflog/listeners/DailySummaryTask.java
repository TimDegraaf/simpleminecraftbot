package me.minercoffee.simpleminecraftbot.stafflog.listeners;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;
import static me.minercoffee.simpleminecraftbot.utils.DataManager.getStaffplaytimeConfig;


public class DailySummaryTask extends BukkitRunnable {
    private final Main plugin;

    public DailySummaryTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        if (calendar.get(Calendar.HOUR_OF_DAY) == 22) {
            try {
                StringBuilder toSend = new StringBuilder();
                for (String uuid : getStaffplaytimeConfig().getKeys(false)) {
                    OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);
                    if (player.getPlayer().hasPermission("simplebotminecraft.staff")  || player.isOp()) return;
                    toSend.append("- **").append(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).append("** has played for **").append(plugin.convertTime(getStaffplaytimeConfig().getLong(uuid))).append("** this week\n");
                }
                System.out.println(toSend);
                plugin.sendstaffonline(Bukkit.getOfflinePlayer("MinerCoffee97"), "**DAILY SUMMARY**\n" + toSend, false, Color.GRAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}