package me.minercoffee.simpleminecraftbot.utils;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {
    private final Main plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    public static FileConfiguration AdvancementsConfig;
    private static File AdvancementsFile;
    public static FileConfiguration staffplaytimeConfig;
    private static File staffplaytimeFile;

    public DataManager(@NotNull Main plugin) {
        this.plugin = plugin;
        StaffplaytimeSetup();
        //saves/initializes
        saveDefaultConfig();
        saveAdvancementsConfig();
        savestaffplaytime();
        AdvancementsConfig = loadCustomConfig("SimpleMinecraftBot", new File(plugin.getDataFolder(), "advancements.yml"));
        staffplaytimeConfig = loadCustomConfig("SimpleMinecraftBot", new File(Main.getInstance().getDataFolder(), "staffplaytime.yml"));
    }

    public void reloadConfig() {
        if (this.configFile == null) return;
        if (dataConfig != null) return;
        this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("config.yml");
        AdvancementsConfig = loadCustomConfig("SimpleMinecraftBot", new File(plugin.getDataFolder(), "advancements.yml"));
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            try {
                this.dataConfig.setDefaults(defaultConfig);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
    }

    public static void saveAdvancementsConfig() {
        AdvancementsFile = new File(Main.getInstance().getDataFolder(), "advancements.yml");
        AdvancementsConfig = loadCustomConfig("SimpleMinecraftBot", new File(Main.getInstance().getDataFolder(), "advancements.yml"));
            try {
                if (AdvancementsConfig != null) {
                    AdvancementsConfig.save(AdvancementsFile);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    public static void StartupAdvancementConfig() {
        AdvancementsFile = new File(Main.getInstance().getDataFolder(), "advancements.yml");
        AdvancementsConfig = loadCustomConfig("SimpleMinecraftBot", new File(Main.getInstance().getDataFolder(), "advancements.yml"));
        InputStream defaultStream = Main.getInstance().getResource("advancements.yml");
        InputStream in =  Main.instance.getResource("advancements.yml");
            if (defaultStream != null) {
                YamlConfiguration AdvanceConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));

                if (in != null) {
                    InputStreamReader inReader = new InputStreamReader(in);
                    AdvancementsConfig.setDefaults(YamlConfiguration.loadConfiguration(inReader));
                    try {
                        AdvancementsConfig.setDefaults(AdvanceConfig);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    AdvancementsConfig.options().copyDefaults(true);
                    try {
                        AdvancementsConfig.save(AdvancementsFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    public static void StaffplaytimeSetup() {
        staffplaytimeFile = new File(Bukkit.getServer().getPluginManager().getPlugin("SimpleMinecraftBot").getDataFolder(), "staffplaytime.yml");

        if (!staffplaytimeFile.exists()){
            try{
                staffplaytimeFile.createNewFile();
            }catch (IOException e){
                //owww
            }
        }
        staffplaytimeConfig = YamlConfiguration.loadConfiguration(staffplaytimeFile);
    }
    public static FileConfiguration getStaffplaytimeConfig(){
        return staffplaytimeConfig;
    }
    public static void ReloadStaffplaytime(){
        staffplaytimeFile = new File(Main.getInstance().getDataFolder(), "staffplaytime.yml");
        staffplaytimeConfig = YamlConfiguration.loadConfiguration(staffplaytimeFile);
    }
    public static void savestaffplaytime() {
        staffplaytimeFile = new File(Main.getInstance().getDataFolder(), "staffplaytime.yml");
        staffplaytimeConfig = loadCustomConfig("SimpleMinecraftBot", new File(Main.getInstance().getDataFolder(), "staffplaytime.yml"));
        try{
            if (staffplaytimeConfig != null) {
                staffplaytimeConfig.save(staffplaytimeFile);
            }
        }catch (IOException e){
            System.out.println("Couldn't save file");
        }
    }

    public static FileConfiguration getadvancementsConfig(){
        AdvancementsFile = new File(Main.getInstance().getDataFolder(), "advancements.yml");
        AdvancementsConfig = loadCustomConfig("SimpleMinecraftBot", new File(Main.getInstance().getDataFolder(), "advancements.yml"));
        return AdvancementsConfig;
    }
    public static void ReloadAdvancements(){
        AdvancementsFile = new File(Main.getInstance().getDataFolder(), "advancements.yml");
        AdvancementsConfig = YamlConfiguration.loadConfiguration(AdvancementsFile);
    }
    public FileConfiguration getConfig() {
        if (this.dataConfig != null){
            reloadConfig();
        }
        return this.dataConfig;
    }
    public void saveAdvancements() {
        if (AdvancementsFile == null) {
            AdvancementsFile = new File(this.plugin.getDataFolder(), "advancements.yml");
        }
        if (AdvancementsConfig != null) return;
        if (AdvancementsFile.exists()) {
            this.plugin.saveResource("advancements.yml", false);
        }
    }
    public static FileConfiguration loadCustomConfig(String resourceName, @NotNull File out){
        try {
            InputStream in =  Main.instance.getResource(resourceName);

            if(!out.exists()) {
                Main.getInstance().getDataFolder().mkdir();
                out.createNewFile();
            }

            FileConfiguration file = YamlConfiguration.loadConfiguration(out);
            if (in != null){
                InputStreamReader inReader = new InputStreamReader(in);
                file.setDefaults(YamlConfiguration.loadConfiguration(inReader));
                file.options().copyDefaults(true);
                file.save(out);
            }
            return file;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public void saveConfig(){
        if (this.dataConfig != null || this.configFile == null)
            return;
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }
    public void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        }
        if (dataConfig != null) return;
        if (!this.configFile.exists()) {
            this.plugin.saveResource("config.yml", false);
        }
    }
}