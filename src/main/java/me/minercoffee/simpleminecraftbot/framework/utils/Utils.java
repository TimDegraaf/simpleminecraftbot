package me.minercoffee.simpleminecraftbot.framework.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
public final class Utils {
    private static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###.##");

    public static String decimalFormat(int i) {
        return df.format((long)i);
    }

    public static String decimalFormat(double i) {
        return df.format(i);
    }

    public static String decimalFormat(float i) {
        return df.format((double)i);
    }

    public static String decimalFormat(@NotNull Object i) {
        return df.format(i);
    }

    public static List<Player> getOnlinePlayers() {
        List<? extends Player> collect = new ArrayList(Bukkit.getOnlinePlayers());
        return new ArrayList(collect);
    }

    public static void executeConsoleCommand(@NotNull String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public static void broadcast(@NotNull String message) {
        Bukkit.broadcastMessage(color(message));
    }

    public static @NotNull String color(@NotNull String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> color(@NotNull List<String> input) {
        List<String> returnValue = new ArrayList();
        input.forEach((s) -> {
            returnValue.add(color(s));
        });
        return returnValue;
    }

    public static boolean hasPlugin(@NotNull String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }

    private Utils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
