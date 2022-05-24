package me.minercoffee.simpleminecraftbot.framework;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

public abstract class BasePlugin extends JavaPlugin {
    public BasePlugin() {
    }

    public void onEnable() {
        FrameworkMessage.init();
    }

    public void loadFiles(@NotNull JavaPlugin plugin, String... names) {
        File dataFolder = plugin.getDataFolder();
        Arrays.stream(names).forEach((name) -> {
            File file = new File(dataFolder, name);
            FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
            if (!file.exists()) {
                FileLoader.loadFile(plugin, name);
            }

            try {
                fileConfig.load(file);
            } catch (Exception var6) {
                System.out.println("Could not load " + name + " due to the following:");
                var6.printStackTrace();
            }

            fileConfig.getKeys(false).forEach((priceString) -> fileConfig.set(priceString, fileConfig.getString(priceString)));
        });
    }

    public void registerCommands(Command... commands) throws NoSuchFieldException {
        VersionChecker versionChecker = VersionChecker.getInstance();
        Server server = Bukkit.getServer();
        Field field = server.getClass().getDeclaredField("commandMap");
        field.setAccessible(true);
        Arrays.stream(commands).forEach((iCommand) -> {
            try {
                CommandMap commandMap = (CommandMap) field.get(server);
                String name = iCommand.getName();
                Command command = commandMap.getCommand(name);
                if (command != null) {
                    Map map;
                    if (versionChecker != null) {
                        if (versionChecker.isLegacy()) {
                            Field commandField = commandMap.getClass().getDeclaredField("knownCommands");
                            commandField.setAccessible(true);
                            map = (Map) commandField.get(commandMap);
                        } else {
                            map = (Map) commandMap.getClass().getDeclaredMethod("getKnownCommands").invoke(commandMap);
                        }

                        command.unregister(commandMap);
                        map.remove(name);
                        iCommand.getAliases().forEach(map::remove);
                    }

                    commandMap.register(name, iCommand);
                }
                } catch(Exception var9){
                    var9.printStackTrace();
                }

        });
    }


    public void registerListeners(Listener... listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Arrays.stream(listeners).forEach((listener) -> pluginManager.registerEvents(listener, this));
    }
}
