package me.minercoffee.simpleminecraftbot;

import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.jeff_media.updatechecker.UserAgentBuilder;
import me.minercoffee.simpleminecraftbot.clearcmd.Clear;
import me.minercoffee.simpleminecraftbot.discord.BotCommands;
import me.minercoffee.simpleminecraftbot.discord.ModalListeners;
import me.minercoffee.simpleminecraftbot.stafflog.cmd.*;
import me.minercoffee.simpleminecraftbot.stafflog.listeners.*;
import me.minercoffee.simpleminecraftbot.stafflog.cmd.staffchat;
import me.minercoffee.simpleminecraftbot.discord.ticket.ButtonListener;
import me.minercoffee.simpleminecraftbot.discord.ticket.SetTicketCMD;
import me.minercoffee.simpleminecraftbot.utils.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.*;

import static me.minercoffee.simpleminecraftbot.utils.Advancements.AdvancementsUpdater;
import static me.minercoffee.simpleminecraftbot.utils.DataManager.*;

public final class Main extends JavaPlugin {
    public PlayerLogListener playerLogListener;
    public static HashMap<UUID, Long> map = new HashMap<>();
    public DataManager data;
    public Embles embles;
    public static void setInstance(Main instance) {
        Main.instance = instance;
    }
    public static Main instance;
    public final Map<String, String> advancementToDisplayMap = new HashMap<>();
    public static JDA jda;
    public TextChannel chatChannel;
    public TextChannel staffplaytimechannel;
    public TextChannel ServerStatuschannel;
    public TextChannel ingamestaffchatchannel;
    public TextChannel commandschannel;
    public static final String PREFIX = "!";
    public static Main getInstance() {
        return instance;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onEnable() {
        super.onEnable();
        this.playerLogListener = new PlayerLogListener(this, embles);
        setInstance(this);
        saveDefaultConfig();
        saveConfig();
        loadConfig();
        ServerUtils();
        DataUpdater();
        embles = new Embles(this);
        MessagesUpdater();
        String botToken = "";
        try {
            ConfigUpdater();
            AdvancementsUpdater();
            jda = JDABuilder.createDefault(botToken).setActivity(Activity.playing("Minecraft")).setStatus(OnlineStatus.ONLINE)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS,GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES).build().awaitReady();
            String chatChannelId = getConfig().getString("ingame-chat");
            if (chatChannelId != null) {
                chatChannel = jda.getTextChannelById(chatChannelId);
            }
            if (chatChannel == null || chatChannelId == null){
                System.out.println("Please fill out the config.yml and restart the server.");
            }
            String ingamestaffchatid = getConfig().getString("staffchat");
            if (ingamestaffchatid != null){
                ingamestaffchatchannel = jda.getTextChannelById(ingamestaffchatid);
            }
            String ServerStatusid = getConfig().getString("serverstatus");
            if (ServerStatusid != null){
                ServerStatuschannel = jda.getTextChannelById(ServerStatusid);
            }
            String staffplaytimechannelid = getConfig().getString("staffplaytime");
            if (staffplaytimechannelid != null) {
                staffplaytimechannel = jda.getTextChannelById(staffplaytimechannelid);
            }
            long commandid = Long.parseLong(Objects.requireNonNull(getConfig().getString("commands")));
                commandschannel = jda.getTextChannelById(commandid);
            Guild guildid = jda.getGuildById(Objects.requireNonNull(getConfig().getString("guild_id")));
            if (guildid != null){
                guildid.upsertCommand("sup", "say wassup to someone").queue();
            }
            new staffchat(this, embles);
            jda.addEventListener(new Discordhelp(this), new DiscordBotPingEvent(this),  new staffchat(this, embles));
            getCommand("staffcheck").setExecutor(new CommandCheck(this));
            getCommand("staffplaytime").setExecutor(new Staffplaytime(this, embles));
            getCommand("staff").setExecutor(new PlayerLogListener(instance, embles));
            getServer().getPluginManager().registerEvents(new PlayerLogListener(instance, embles), this);
            getCommand("staffhome").setExecutor(new Homes());
            getServer().getPluginManager().registerEvents(new UpdateCheckListener(this), this);
            new DateCheckRunnable(this, embles).runTaskTimerAsynchronously(this, 0L, 60L * 20L);
            new PlayerSaveTask().runTaskTimerAsynchronously(this, 0L, 60 * 20L);
            new DailySummaryTask(this, embles).runTaskTimerAsynchronously(this, 0L, 60L * 20L);
            jda.addEventListener(new Clear(this), new BotCommands(), new ModalListeners());
            jda.addEventListener(new ButtonListener(this));
            jda.addEventListener(new SetTicketCMD());
            jda.addEventListener(new DiscordListener());
            jda.addEventListener(new OnlineStaff(this));
            jda.addEventListener(new DiscordCommandCheckLegacy(this));
            getCommand("ping").setExecutor(new Ping());
            new reloadcmd(this);
            getServer().getPluginManager().registerEvents(new ServerChatListener(this, embles), this);
            getCommand("updatechecker").setExecutor(new UpdateCheckCommand());
            getCommand("sbreload").setExecutor(new reloadcmd(this));
            this.data = new DataManager(this);
            embles.ServerRestartingOn(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is online.", true, Color.GREEN);
            embles.ServerisOnline(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is online.", true, Color.RED);
            //advancement config getting the names.
            ConfigurationSection advancementMap = getadvancementsConfig().getConfigurationSection("advancementMap");
            if (advancementMap != null) {
                    for (String key : advancementMap.getKeys(false)) {
                        advancementToDisplayMap.put(key, advancementMap.getString(key));
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ServerUtils(){
        int pluginId = 16255;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SingleLineChart("players", () -> Bukkit.getOnlinePlayers().size()));
        metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            String javaVersion = System.getProperty("java.version");
            Map<String, Integer> entry = new HashMap<>();
            entry.put(javaVersion, 1);
            if (javaVersion.startsWith("1.7")) {
                map.put("Java 1.7", entry);
            } else if (javaVersion.startsWith("1.8")) {
                map.put("Java 1.8", entry);
            } else if (javaVersion.startsWith("1.9")) {
                map.put("Java 1.9", entry);
            } else {
                map.put("Other", entry);
            }
            return map;
        }));
        new UpdateChecker(this, UpdateCheckSource.CUSTOM_URL, "https://github.com/MinerCoffee/simpleminecraftbot/blob/master/src/main/resources/latestversion.txt")
                .setDownloadLink("https://discord.com/channels/941600403513040916/941600403513040919")
                .setChangelogLink("https://discord.gg/5nDbUY2qFy")
                .setDonationLink("https://www.paypal.com/paypalme/MinerCoffee")
                .setNotifyOpsOnJoin(true)
                .setUserAgent(new UserAgentBuilder().addPluginNameAndVersion())
                .setColoredConsoleOutput(true)
                .checkEveryXHours(24)
                .checkNow();
    }
    public void ConfigUpdater(){
        instance.getConfig().addDefault("staffplaytime", "");
        instance.getConfig().addDefault("serverstatus", "");
        instance.getConfig().addDefault("ingame-chat", "");
        instance.getConfig().addDefault("staffchat", "");
        instance.getConfig().addDefault("commandschannel", "");
        instance.getConfig().addDefault("category_id", "");
        instance.getConfig().addDefault("roles_player_id", "");
        instance.getConfig().addDefault("roles_staff_id", "");
        instance.getConfig().addDefault("roles_owner_id", "");
        instance.getConfig().addDefault("Status_enable", true);
        instance.getConfig().addDefault("bot-link", "https://discord.com/api/oauth2/authorize?client_id=966786459937964062&permissions=8&scope=bot");
        instance.getConfig().addDefault("bot-prefix", "!");
        instance.getConfig().addDefault("guild_id", "");
        instance.getConfig().addDefault("staffplaytime-timer.daily-hour", "22");
        instance.getConfig().addDefault("staffplaytime-timer.weekly-hour", "22");
        instance.getConfig().addDefault("staffplaytime-timer.weekly-day", "0");
        instance.saveConfig();
    }
    public void MessagesUpdater(){
        getMessagesConfig().addDefault("set-message", "&aSetting a temporary home @: &7");
        getMessagesConfig().addDefault("override-message", "&aOverriding current home @: &7");
        getMessagesConfig().addDefault("return-message", "&aReturned to former location.");
        SaveMessageConfig();
    }
    public void DataUpdater(){
        getDataConfig().addDefault("staff-homes-locations", "");
    }

    public void loadConfig(){
        getConfig().options().copyDefaults(true);
        instance.saveConfig();
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onDisable() {
        (new PlayerLogListener(instance, embles)).saveAllPlayers();
        try {
           embles.ServerRestartingOff(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is offline.", true, Color.RED);
           embles.Serverisoffline(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is Restarting.", true, Color.RED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Contract(pure = true)
    public @NotNull String convertTime(Long ms) {
        int seconds = (int) (ms / 1000) % 60;
        int minutes = (int) ((ms / (1000 * 60)) % 60);
        int hours = (int) ((ms / (1000 * 60 * 60)) % 24);
        return hours + " hours, " + minutes + " minutes and " + seconds + " seconds";
    }
    public final class DiscordListener extends ListenerAdapter {
        @Override
        public void onMessageReceived(@NotNull MessageReceivedEvent e){
            if (!e.getChannel().equals(chatChannel)) return;
            Member member = e.getMember();
            if (member == null || member.getUser().isBot()) return;

            String message = e.getMessage().getContentDisplay();
            Bukkit.broadcastMessage(ChatColor.BOLD + "<" + member.getEffectiveName() + ">" + ChatColor.GRAY + " " + message);
        }
    }
    public static String getPREFIX() {
        return PREFIX;
    }

    public static HashMap<UUID, Long> getMap() {
        return map;
    }
}