package me.minercoffee.simpleminecraftbot.framework;

import org.bukkit.Bukkit;

public class VersionChecker {
    private static VersionChecker instance;
    private final ServerVersion serverVersion;

    public VersionChecker() {
        instance = this;
        this.serverVersion = ServerVersion.fromServerPackageName(Bukkit.getServer().getClass().getName());
    }

    public boolean isLegacy() {
        return this.serverVersion.isAtMost(ServerVersion.V1_17);
    }

    public boolean isModern() {
        return this.serverVersion.isAtLeast(ServerVersion.V1_18);
    }

    public static VersionChecker getInstance() {
        return instance;
    }

    public ServerVersion getServerVersion() {
        return this.serverVersion;
    }
}
