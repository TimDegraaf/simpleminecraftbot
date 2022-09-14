package me.minercoffee.simpleminecraftbot.utils;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ColorMsg {
    public ColorMsg(){
    }
    @Contract("_ -> new")
    public static @NotNull String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
