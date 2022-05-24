package me.minercoffee.simpleminecraftbot.stafflog;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static me.minercoffee.simpleminecraftbot.Main.instance;

public class ConfigUtils {
    private final File file;
    private final Configuration config;
    public ConfigUtils(String filename){
        file = new File(instance.getDataFolder(), filename + ".yml");
        config = YamlConfiguration.loadConfiguration(file);
    }
    public void save(){
        try {
            instance.getConfig().save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
