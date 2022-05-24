package me.minercoffee.simpleminecraftbot;

import me.minercoffee.simpleminecraftbot.clearcmd.Clear;
import me.minercoffee.simpleminecraftbot.framework.BasePlugin;
import me.minercoffee.simpleminecraftbot.stafflog.cmd.CommandCheck;
import me.minercoffee.simpleminecraftbot.stafflog.cmd.DiscordCommandCheckLegacy;
import me.minercoffee.simpleminecraftbot.stafflog.cmd.Discordhelp;
import me.minercoffee.simpleminecraftbot.stafflog.listeners.*;
import me.minercoffee.simpleminecraftbot.ticket.ButtonListener;
import me.minercoffee.simpleminecraftbot.ticket.commands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.time.LocalDate;
import java.util.*;

public final class Main extends BasePlugin {

    public HashMap<UUID, Long> map = new HashMap<>();

    public static void setInstance(Main instance) {
        Main.instance = instance;
    }

    public static Main instance;
    public final Map<String, String> advancementToDisplayMap = new HashMap<>();
    public static JDA jda;
    private TextChannel chatChannel;

    private TextChannel BotOnlineChannel;
    private TextChannel staffchannel;

    private static final String PREFIX = "!";
    public static Main getInstance() {
        return instance;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onEnable() {
        setInstance(this);
        saveDefaultConfig();
        String botToken = getConfig().getString("bot-token");
        try {
            jda = JDABuilder.createDefault(botToken).setActivity(Activity.playing("Minecraft")).setStatus(OnlineStatus.ONLINE).build().awaitReady();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
        }
        if (jda == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        String BotChannelID = getConfig().getString("BotChannelID");
        if (BotChannelID != null) {
            BotOnlineChannel = jda.getTextChannelById(BotChannelID);
        }
        String chatChannelId = getConfig().getString("chat-channel-id");
        if (chatChannelId != null) {
            chatChannel = jda.getTextChannelById(chatChannelId);
        }
        if (BotChannelID != null) {
            this.BotOnlineChannel = jda.getTextChannelById(BotChannelID);
        }
        jda.addEventListener(new DiscordCommandCheckLegacy(), new Discordhelp(), new DiscordBotPingEvent());
        getCommand("staffcheck").setExecutor(new CommandCheck(this));
        instance.registerListeners(new PlayerLogListener(this));
        new DateCheckRunnable(this).runTaskTimerAsynchronously(this, 0L, 3600L * 20L);
        new PlayerSaveTask().runTaskTimerAsynchronously(this, 0L, 120L * 20L);
        new DailySummaryTask(this).runTaskTimerAsynchronously(this, 0L, 86400L * 20L);
        jda.addEventListener(new Clear(this));
        jda.addEventListener(new ButtonListener(this));
        jda.addEventListener(new commands());
        jda.addEventListener(new DiscordListener());
        getServer().getPluginManager().registerEvents(new SpigotListener(), this);
        new ReloadCommand(this);
        this.BotSendEmbed(Bukkit.getOfflinePlayer("MinerCoffee97"), "Bot online", true, Color.GREEN);

        //advancement config getting the names.
        ConfigurationSection advancementMap = getConfig().getConfigurationSection("advancementMap");
        if (advancementMap != null){
            for (String key : advancementMap.getKeys(false)){
                advancementToDisplayMap.put(key, advancementMap.getString(key));
            }
        }
        String staffchannelid = getConfig().getString("Staff-channel");
        if (staffchannelid != null) {
            staffchannel = jda.getTextChannelById(staffchannelid);
        }
        sendstaffEmbed(Bukkit.getOfflinePlayer("MinerCoffee97"), "Bot online", true, Color.RED);
    }

    @Override
    public void onDisable() {
        if (jda != null) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            jda.shutdownNow();
        }
    }
    public void sendstaffEmbed(OfflinePlayer player, String content, boolean contentAuthorLine, Color color) {
        if (staffchannel == null) return;

        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.GREEN);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        LocalDate ld = LocalDate.now();
        builder.setFooter(ld.getDayOfMonth() + "/" + ld.getMonthValue() + "/" + ld.getYear() + " (day/month/year)");

        staffchannel.sendMessageEmbeds(builder.build()).queue();
    }

    public void sendOfflineEmbed(OfflinePlayer player, String content, boolean contentAuthorLine, Color color) {
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        LocalDate ld = LocalDate.now();
        builder.setFooter(ld.getDayOfMonth() + "/" + ld.getMonthValue() + "/" + ld.getYear() + " (day/month/year)");
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public String convertTime(Long ms) {
        int seconds = (int) (ms / 1000) % 60;
        int minutes = (int) ((ms / (1000 * 60)) % 60);
        int hours = (int) ((ms / (1000 * 60 * 60)) % 24);

        return hours + " hours, " + minutes + " minutes and "
                + seconds + " seconds";

    }

    public Role checkMemberRoles(Member member, String name) {
        List<Role> roles = member.getRoles();
        return roles.stream()
                .filter(role -> role.getName().equals(name)) // filter by role name
                .findFirst() // take first result
                .orElse(null); // else return null
    }

    public void BotSendEmbed(OfflinePlayer player, String content, boolean contentAuthorLine, Color color) {
        if (BotOnlineChannel != null) {
            EmbedBuilder builder = (new EmbedBuilder()).setAuthor(contentAuthorLine ? content : player.getName(), null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
            builder.setColor(java.awt.Color.GREEN);
            if (!contentAuthorLine) {
                builder.setDescription(content);
            }
            LocalDate ld = LocalDate.now();
            builder.setFooter(ld.getDayOfMonth() + "/" + ld.getMonthValue() + "/" + ld.getYear() + " (day/month/year)");
            chatChannel.sendMessageEmbeds(builder.build(), new MessageEmbed[0]).queue();
        }
    }

    private void sendMessage(Player player, String content, Boolean contentInAuthorLine, Color color) {
        if (chatChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor(
                contentInAuthorLine ? content : player.getName(), null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1"
        );
        builder.setColor(java.awt.Color.GREEN);

        if (!contentInAuthorLine) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void sendMsg(Player player, String content, Boolean contentInAuthorLine, Color color) {
        if (chatChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor(
                contentInAuthorLine ? content : player.getName(), null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1" //can be plater.getName or player.getDisplayName
        );
        builder.setColor(java.awt.Color.GREEN);

        if (!contentInAuthorLine) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }


    public class SpigotListener implements Listener {
        @SuppressWarnings("deprecation")
        @EventHandler
        public void onServerCommand(ServerCommandEvent event, Player p) {
            FileConfiguration spigot = YamlConfiguration.loadConfiguration(new File(Bukkit.getServer().getWorldContainer(), "spigot.yml"));

            if (event.getCommand().equalsIgnoreCase("reload")) {
                // Restarts server if server is set up for it.
                if (spigot.getBoolean("settings.restart-on-crash")) {
                    sendOfflineEmbed((Bukkit.getOfflinePlayer("MinerCoffee97")), "Bot off", true, Color.RED);
                    Bukkit.getLogger().severe("Restarting server due to reload command!");
                    event.setCommand("restart");
                } else {
                    // Call to server shutdown on disable.
                    // Won't hurt if server already disables itself, but will prevent plugin unload/reload.
                    sendOfflineEmbed((Bukkit.getOfflinePlayer("MinerCoffee97")), "Bot off", true, Color.RED);
                    Bukkit.getLogger().severe("Stopping server due to reload command!");
                    Bukkit.shutdown();
                }
            }
        }
        @EventHandler
        public void onChat(AsyncPlayerChatEvent e) {
            Player player = e.getPlayer();
            sendMsg(player, e.getMessage(), false, Color.GRAY);
        }
        @EventHandler
        public void onJoin(PlayerJoinEvent e){
            Player player = e.getPlayer();
            sendMessage(player, e.getPlayer().getName() + " joined the game.", true, Color.GREEN);
        }
        @EventHandler
        public void onQuit(PlayerQuitEvent e){
            Player player = e.getPlayer();
            sendMessage(e.getPlayer(), player.getName() + " left the game.", true, Color.RED);
        }
        @EventHandler
        public void onDeath(PlayerDeathEvent e){
            Player p = e.getEntity();
            String deathMessage = e.getDeathMessage() == null ? p.getName() + " died." : e.getDeathMessage();
            sendMessage(p, deathMessage, true, Color.GRAY);
        }
        @EventHandler
        public void onAdvancement(PlayerAdvancementDoneEvent e){
            Player player = e.getPlayer();
            String advancementKey  = e.getAdvancement().getKey().getKey();
            String display = advancementToDisplayMap.get(advancementKey);
            if(display == null ) return;
            sendMessage(player, player.getName() + " has made the advancement ["+ display + "]", true, Color.TEAL);
        }
    }
    public class ReloadCommand implements CommandExecutor {
        Main plugin;
        ReloadCommand(Main plugin){
            this.plugin = plugin;
            Objects.requireNonNull(getCommand("smreload")).setExecutor(this);
        }
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            Player p = (Player) sender;
            if (p.isOp()) {
                plugin.reloadConfig();
                sender.sendMessage("You have reloaded the config!");
            }
            return true;
        }
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

    private String Color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public HashMap<UUID, Long> getMap() {
        return map;
    }
}

