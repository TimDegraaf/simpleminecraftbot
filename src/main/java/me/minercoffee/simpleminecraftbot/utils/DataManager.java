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
import java.util.logging.Level;

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
    public static FileConfiguration DataConfig;
    public static File DataFile;

    public DataManager(@NotNull Main plugin) {
        this.plugin = plugin;
        StaffplaytimeSetup();
        //saves/initializes
        MessagesSetup();
        SaveMessageConfig();
        saveDefaultConfig();
        saveAdvancementsConfig();
        savestaffplaytime();
        DataConfigSetup();
        StartupAdvancementConfig();
        AdvancementsConfig = loadFromResource("advancements.yml", new File(plugin.getDataFolder(), "advancements.yml"));
        messagesConfig = loadFromResource("messages.yml", new File(plugin.getDataFolder(), "messages.yml"));
        DataConfig = loadFromResource("data.yml", new File(plugin.getDataFolder(), "data.yml"));
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
    public static FileConfiguration getDataConfig(){
        if (DataConfig == null){
            DataReload();
        }
        return DataConfig;
    }
    public static void DataConfigSetup() {
        if (DataFile == null){
            DataFile = new File(Main.getInstance().getDataFolder(), "data.yml");
        }
        if (!DataFile.exists()){
            Main.getInstance().saveResource("data.yml", false);
        }
    }
    public static void DataReload(){
        DataConfig = loadFromResource("data.yml", new File(Main.getInstance().getDataFolder(), "data.yml"));
        if (DataFile == null){
            DataFile = new File(Main.getInstance().getDataFolder(), "data.yml");
        }
        DataConfig = YamlConfiguration.loadConfiguration(DataFile);
        InputStream defaultStream = Main.getInstance().getResource("data.yml");
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            DataConfig.setDefaults(defaultConfig);
        }
    }
    public static void SaveData() {
        if (DataConfig == null || DataFile == null) return;
        try {
            DataConfig.save(DataFile);
        } catch (IOException e) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Could not save data to " + DataFile, e);
        }
    }
    public static FileConfiguration getMessagesConfig(){
        if (messagesConfig == null){
            MessagesReload();
        }
        return messagesConfig;
    }
    public static void MessagesReload(){
        messagesConfig = loadFromResource("messages.yml", new File(Main.getInstance().getDataFolder(), "messages.yml"));
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
    public static void savestaffplaytime() {
        try {
            if (staffplaytimeConfig != null) {
                staffplaytimeConfig.save(staffplaytimeFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void StartupAdvancementConfig() {
        if (AdvancementsFile == null){
            AdvancementsFile = new File(Main.getInstance().getDataFolder(), "advancements.yml");
        }
        if (!AdvancementsFile.exists()){
            Main.getInstance().saveResource("advancements.yml", false);
        }
        AdvancementsConfig = YamlConfiguration.loadConfiguration(AdvancementsFile);
    }

    public static FileConfiguration getadvancementsConfig(){
        if (AdvancementsConfig == null){
            ReloadAdvancements();
        }
        if (AdvancementsConfig != null){
            ReloadAdvancements();
        }
        return AdvancementsConfig;
    }
    public static void ReloadAdvancements(){
        AdvancementsConfig = loadFromResource("advancements.yml", new File(Main.getInstance().getDataFolder(), "advancements.yml"));
        if (AdvancementsFile == null){
            AdvancementsFile = new File(Main.getInstance().getDataFolder(), "advancements.yml");
        }
        AdvancementsConfig = YamlConfiguration.loadConfiguration(AdvancementsFile);
        InputStream defaultStream = Main.getInstance().getResource("advancements.yml");
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            AdvancementsConfig.setDefaults(defaultConfig);
        }
    }
    public static void saveAdvancementsConfig() {
        if (AdvancementsConfig == null || AdvancementsFile == null) return;

        try {
            AdvancementsConfig.save(AdvancementsFile);
        } catch (IOException e) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Could not save advancements to " + AdvancementsFile, e);
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
    public static @Nullable FileConfiguration loadFromResource(String resourceName, @NotNull File out){
        try {
            InputStream in = Main.getInstance().getResource(resourceName);

            if (!out.exists()){
                Main.getInstance().getDataFolder().mkdir();
                out.createNewFile();
            }
            FileConfiguration file = YamlConfiguration.loadConfiguration(out);
            if (in != null){
                InputStreamReader inReader =  new InputStreamReader(in);
                file.setDefaults(YamlConfiguration.loadConfiguration(inReader));
                file.options().copyDefaults(true);
                file.save(out);
            }
            return file;
        } catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
}