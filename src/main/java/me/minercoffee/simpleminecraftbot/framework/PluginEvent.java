package me.minercoffee.simpleminecraftbot.framework;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class PluginEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public PluginEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
