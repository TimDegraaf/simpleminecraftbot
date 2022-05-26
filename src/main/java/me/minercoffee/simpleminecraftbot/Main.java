package me.minercoffee.simpleminecraftbot;

import me.minercoffee.simpleminecraftbot.clearcmd.Clear;
import me.minercoffee.simpleminecraftbot.framework.BasePlugin;
import me.minercoffee.simpleminecraftbot.stafflog.cmd.CommandCheck;
import me.minercoffee.simpleminecraftbot.stafflog.cmd.DiscordCommandCheckLegacy;
import me.minercoffee.simpleminecraftbot.stafflog.cmd.Discordhelp;
import me.minercoffee.simpleminecraftbot.stafflog.listeners.*;
import me.minercoffee.simpleminecraftbot.ticket.ButtonListener;
import me.minercoffee.simpleminecraftbot.ticket.commands;
import me.minercoffee.simpleminecraftbot.utils.UpdateCheckCommand;
import me.minercoffee.simpleminecraftbot.utils.UpdateCheckListener;
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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
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
    private TextChannel onlinechanel;

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
        String chatChannelId = getConfig().getString("chat-channel-id");
        if (chatChannelId != null) {
            chatChannel = jda.getTextChannelById(chatChannelId);
        }
        if (BotChannelID != null) {
            this.onlinechanel = jda.getTextChannelById(BotChannelID);
        }
        String onlinechanelid = getConfig().getString("onlinechannel");
        if(onlinechanelid != null){
            onlinechanel = jda.getTextChannelById(onlinechanelid);
        }
        jda.addEventListener(new DiscordCommandCheckLegacy(), new Discordhelp(), new DiscordBotPingEvent());
        Objects.requireNonNull(getCommand("staffcheck")).setExecutor(new CommandCheck(this));
        instance.registerListeners(new PlayerLogListener(this));
        getServer().getPluginManager().registerEvents(new UpdateCheckListener(this), this);
        new DateCheckRunnable(this).runTaskTimerAsynchronously(this, 0L, 3600L * 20L);
        new PlayerSaveTask().runTaskTimerAsynchronously(this, 0L, 60L * 20L);
        new DailySummaryTask(this).runTaskTimerAsynchronously(this, 0L, 86400L * 20L);
        jda.addEventListener(new Clear(this));
        jda.addEventListener(new ButtonListener(this));
        jda.addEventListener(new commands());
        jda.addEventListener(new DiscordListener());
        getServer().getPluginManager().registerEvents(new SpigotListener(), this);
        Objects.requireNonNull(this.getCommand("updatechecker")).setExecutor(new UpdateCheckCommand());
        new ReloadCommand(this);
        this.BotSendEmbed(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is online.", true, Color.GREEN);
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
        sendstaffEmbed(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is online.", true, Color.GREEN);
    }
    private void purgeMessages(TextChannel channel) {
        try {
            MessageHistory history = new MessageHistory(channel);
            List<Message> msg;
            msg = history.retrievePast(2).complete();
            channel.deleteMessages(msg).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onDisable() {
        (new PlayerLogListener(this)).saveAllPlayers();
        BotisOfflineembed(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is offline.", true, Color.RED);
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

    public void BotisOfflineembed(OfflinePlayer player, String content, boolean contentAuthorLine, Color color) {
        if (onlinechanel == null) return;
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
        onlinechanel.sendMessageEmbeds(builder.build()).queue();
        purgeMessages(onlinechanel);
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
        if (onlinechanel != null) {
            EmbedBuilder builder = (new EmbedBuilder()).setAuthor(contentAuthorLine ? content : player.getName(), null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
            builder.setColor(java.awt.Color.GREEN);
            if (!contentAuthorLine) {
                builder.setDescription(content);
            }
            LocalDate ld = LocalDate.now();
            builder.setFooter(ld.getDayOfMonth() + "/" + ld.getMonthValue() + "/" + ld.getYear() + " (day/month/year)");
            onlinechanel.sendMessageEmbeds(builder.build(), new MessageEmbed[0]).queue();
            purgeMessages(onlinechanel);
        }
    }

    private void sendMessage(Player player, String content, Color color) {
        if (chatChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor(
                content, null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1"
        );
        builder.setColor(java.awt.Color.GREEN);

        if (!(Boolean) true) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void sendMsg(Player player, String content) {
        if (chatChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor(
                player.getName(), null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1" //can be plater.getName or player.getDisplayName
        );
        builder.setColor(java.awt.Color.GREEN);

        if (!(Boolean) false) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void sendoffmsg(Player player, String content, Color color) {
        if (chatChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor(
                content, null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1"
        );
        builder.setColor(java.awt.Color.RED);

        if (!(Boolean) true) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }


    public class SpigotListener implements Listener {
        @EventHandler
        public void onChat(AsyncPlayerChatEvent e) {
            Player player = e.getPlayer();
            sendMsg(player, e.getMessage());
        }
        @EventHandler
        public void onJoin(PlayerJoinEvent e){
            Player player = e.getPlayer();
            sendMessage(player, e.getPlayer().getName() + " joined the game.", Color.GREEN);
        }
        @EventHandler
        public void onQuit(PlayerQuitEvent e){
            Player player = e.getPlayer();
            sendoffmsg(e.getPlayer(), player.getName() + " left the game.", Color.RED);
        }
        @EventHandler
        public void onDeath(PlayerDeathEvent e){
            Player p = e.getEntity();
            String deathMessage = e.getDeathMessage() == null ? p.getName() + " died." : e.getDeathMessage();
            sendMessage(p, deathMessage, Color.GRAY);
        }
        @EventHandler
        public void onAdvancement(PlayerAdvancementDoneEvent e){
            Player player = e.getPlayer();
            String advancementKey  = e.getAdvancement().getKey().getKey();
            String display = advancementToDisplayMap.get(advancementKey);
            if(display == null ) return;
            sendMessage(player, player.getName() + " has made the advancement ["+ display + "]", Color.TEAL);
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

