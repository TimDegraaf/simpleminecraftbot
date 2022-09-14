package me.minercoffee.simpleminecraftbot.utils;

import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class DataManager {
    private final Main plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    public static FileConfiguration AdvancementsConfig;
    private static File AdvancementsFile;
    public static FileConfiguration staffplaytimeConfig;
    private static File staffplaytimeFile;

    public static FileConfiguration messagesConfig;
    public static File messageFile;

    public DataManager(@NotNull Main plugin) {
        this.plugin = plugin;
        StaffplaytimeSetup();
        //saves/initializes
        MessagesSetup();
        SaveMessageConfig();
        saveDefaultConfig();
        saveAdvancementsConfig();
        savestaffplaytime();
        AdvancementsConfig = loadCustomConfig("SimpleMinecraftBot", new File(plugin.getDataFolder(), "advancements.yml"));
        staffplaytimeConfig = loadCustomConfig("SimpleMinecraftBot", new File(Main.getInstance().getDataFolder(), "staffplaytime.yml"));
        messagesConfig = loadCustomConfig("SimpleMinecraftBot", new File(Main.getInstance().getDataFolder(), "messages.yml"));
    }

    public void reloadConfig() {
        if (this.configFile == null) return;
        if (dataConfig != null) return;
        this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("config.yml");
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
    public static FileConfiguration getMessagesConfig(){
        if (messagesConfig == null){
            MessagesReload();
        }
        return messagesConfig;
    }
    public static void MessagesReload(){
        if (messageFile == null){
            messageFile = new File(Main.getInstance().getDataFolder(), "messages.yml");
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messageFile);
        InputStream defaultStream = Main.getInstance().getResource("messages.yml");
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            messagesConfig.setDefaults(defaultConfig);
        }
    }
    public static void MessagesSetup() {
        if (messageFile == null){
            messageFile = new File(Main.getInstance().getDataFolder(), "messages.yml");
        }
        if (!messageFile.exists()){
            Main.getInstance().saveResource("messages.yml", false);
        }
    }
    public static void SaveMessageConfig() {
        if (messagesConfig == null || messageFile == null) return;
        try {
            messagesConfig.save(messageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void savestaffplaytime() {
        try {
            if (staffplaytimeConfig != null) {
                staffplaytimeConfig.save(staffplaytimeFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public static @Nullable FileConfiguration loadCustomConfig(String resourceName, @NotNull File out){
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