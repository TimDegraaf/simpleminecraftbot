package me.minercoffee.simpleminecraftbot;

import me.minercoffee.simpleminecraftbot.afk.AFKManager;
import me.minercoffee.simpleminecraftbot.afk.listeners.AFKListener;
import me.minercoffee.simpleminecraftbot.afk.tasks.MovementChecker;
import me.minercoffee.simpleminecraftbot.stafflog.cmd.*;
import me.minercoffee.simpleminecraftbot.stafflog.listeners.*;
import me.minercoffee.simpleminecraftbot.ticket.ButtonListener;
import me.minercoffee.simpleminecraftbot.ticket.commands;
import me.minercoffee.simpleminecraftbot.utils.DataManager;
import me.minercoffee.simpleminecraftbot.utils.UpdateCheckCommand;
import me.minercoffee.simpleminecraftbot.utils.UpdateCheckListener;
import me.minercoffee.simpleminecraftbot.utils.reloadcmd;
import net.dv8tion.jda.api.EmbedBuilder;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDate;
import java.util.*;

public final class Main extends JavaPlugin {
    private AFKManager afkManger;
    private PlayerLogListener playerlog;

    public HashMap<UUID, Long> map = new HashMap<>();

    public DataManager data;
    public static void setInstance(Main instance) {
        Main.instance = instance;
    }

    public static Main instance;
    public final Map<String, String> advancementToDisplayMap = new HashMap<>();
    public static JDA jda;
    private TextChannel chatChannel;

    private TextChannel staffchannel;

    private TextChannel ServerStatuschannel;

    private static final String PREFIX = "!";
    public static Main getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        super.onEnable();
        setInstance(this);
        saveDefaultConfig();
        String botToken = "null";

        try {
            afkManger = new AFKManager(playerlog);
            playerlog = new PlayerLogListener(afkManger, this);
            AdvancementsMsg();
            jda = JDABuilder.createDefault(botToken).setActivity(Activity.playing("Minecraft")).setStatus(OnlineStatus.ONLINE)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES).build().awaitReady();
            String chatChannelId = "966782706165882951";
            chatChannel = jda.getTextChannelById(chatChannelId);
            String ServerStatus = "979621646870650960";
            ServerStatuschannel =jda.getTextChannelById(ServerStatus);
            String staffplaytimechannel = "965844461772996628";
            staffchannel = jda.getTextChannelById(staffplaytimechannel);
            jda.addEventListener(new Discordhelp(), new DiscordBotPingEvent());
            getCommand("staffcheck").setExecutor(new CommandCheck(this));
            getCommand("staffplaytime").setExecutor(new Staffplaytime(this));
            getCommand("staff").setExecutor(new PlayerLogListener(afkManger, instance));
            getServer().getPluginManager().registerEvents(new PlayerLogListener(afkManger, instance), this);
            getServer().getPluginManager().registerEvents(new UpdateCheckListener(this), this);
            new DateCheckRunnable(this).runTaskTimerAsynchronously(this, 0L, 60L * 20L);
            new PlayerSaveTask().runTaskTimerAsynchronously(this, 0L, 60 * 20L);
            new DailySummaryTask(this).runTaskTimerAsynchronously(this, 0L, 60L * 20L);
            // jda.addEventListener(new Clear(this));
            jda.addEventListener(new ButtonListener(this));
            jda.addEventListener(new commands());
            jda.addEventListener(new DiscordListener());
            jda.addEventListener(new OnlineStaff(this));
            jda.addEventListener(new DiscordCommandCheckLegacy());
            new reloadcmd(this);
            AFK();
            getServer().getPluginManager().registerEvents(new SpigotListener(), this);
            getCommand("updatechecker").setExecutor(new UpdateCheckCommand());
            getCommand("sbreload").setExecutor(new reloadcmd(this));
            this.data = new DataManager(this);
            this.BotSendEmbed(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is online.", true, Color.GREEN);
            ServerisOnline(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is online.", true, Color.RED);
            //advancement config getting the names.
            ConfigurationSection advancementMap = getConfig().getConfigurationSection("advancementMap");
            if (advancementMap != null) {
                try {
                    for (String key : advancementMap.getKeys(false)) {
                        advancementToDisplayMap.put(key, advancementMap.getString(key));
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void purgeMessages (TextChannel channel) {
        MessageHistory history = new MessageHistory(channel);
        List<Message> msg;
        try {
            msg = history.retrievePast(2).complete();
            if (msg != null) {
                channel.deleteMessages(msg).queue();
            }
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
    public static void AdvancementsMsg(){
        instance.getConfig().addDefault("advancementMap", true);
        instance.getConfig().addDefault("advancementMap.story/mine_stone", "Stone Age");
        instance.getConfig().addDefault("advancementMap.story/upgrade_tools", "Getting an Upgrade");
        instance.getConfig().addDefault("advancementMap.story/smelt_iron", "Acquire Hardware");
        instance.getConfig().addDefault("advancementMap.story/obtain_armor", "Suit Up");
        instance.getConfig().addDefault("advancementMap.story/lava_bucket", "Hot Stuff");
        instance.getConfig().addDefault("advancementMap.story/iron_tools", "Isn't It Iron Pick");
        instance.getConfig().addDefault("advancementMap.story/deflect_arrow", "Not Today, Thank You");
        instance.getConfig().addDefault("advancementMap.story/form_obsidian", "Ice Bucket Challenge");
        instance.getConfig().addDefault("advancementMap.story/mine_diamond", "Diamonds!");
        instance.getConfig().addDefault("advancementMap.story/enter_the_nether", "We Need To Go Deeper");
        instance.getConfig().addDefault("advancementMap.story/shiny_gear", "Cover Me With Diamonds");
        instance.getConfig().addDefault("advancementMap.story/enchant_item", "Enchanter!");
        instance.getConfig().addDefault("advancementMap.story/cure_zombie_villager", "Zombie Doctor!");
        instance.getConfig().addDefault("advancementMap.story/follow_ender_eye", "Eye Spy");
        instance.getConfig().addDefault("advancementMap.story/enter_the_end", "The End?");
        instance.getConfig().addDefault("advancementMap.nether/return_to_sender", "Return to Sender");
        instance.getConfig().addDefault("advancementMap.nether/find_bastion", "Those Were the Days");
        instance.getConfig().addDefault("advancementMap.nether/obtain_ancient_debris", "Hidden in the Depths");
        instance.getConfig().addDefault("advancementMap.nether/fast_travel", "Subspace Bubble");
        instance.getConfig().addDefault("advancementMap.nether/find_fortress", "A Terrible Fortress!");
        instance.getConfig().addDefault("advancementMap.nether/obtain_crying_obsidian", "Who is Cutting Onions?!");
        instance.getConfig().addDefault("advancementMap.nether/distract_piglin", "Oh Shiny!");
        instance.getConfig().addDefault("advancementMap.nether/ride_strider", "This Boat Has Legs!");
        instance.getConfig().addDefault("advancementMap.nether/uneasy_alliance", "Uneasy Alliance!");
        instance.getConfig().addDefault("advancementMap.nether/loot_bastion", "War Pigs!");
        instance.getConfig().addDefault("advancementMap.nether/use_lodestone", "Country Lode, Take Me Home");
        instance.getConfig().addDefault("advancementMap.nether/netherite_armor", "Cover Me in Debris!");
        instance.getConfig().addDefault("advancementMap.nether/get_wither_skull", "Spooky Scary Skeleton!");
        instance.getConfig().addDefault("advancementMap.nether/obtain_blaze_rod", "Into Fire!");
        instance.getConfig().addDefault("advancementMap.nether/charge_respawn_anchor", "Not Quite Nine Lives!");
        instance.getConfig().addDefault("advancementMap.nether/explore_nether", "Hot Tourist Destinations!");
        instance.getConfig().addDefault("advancementMap.nether/summon_wither", "Withering Heights");
        instance.getConfig().addDefault("advancementMap.nether/brew_potion", "Bring Home the Beacon"); //check msg on wiki
        instance.getConfig().addDefault("advancementMap.nether/create_beacon", "Bring Home the Beacon");
        instance.getConfig().addDefault("advancementMap.nether/all_potions", "A Furious Cocktail");



    }

    @Override
    public void onDisable() {
        (new PlayerLogListener(afkManger, instance)).saveAllPlayers();
        try {
            BotisOfflineembed(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is offline.", true, Color.RED);
            Serverisoffline(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is Restarting.", true, Color.RED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void sendstaffonline(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
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
    public void senddtaffoffline(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (staffchannel == null) return;
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

        staffchannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void sendStaffCurrentTime(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (staffchannel == null) return;

        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.orange);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        LocalDate ld = LocalDate.now();
        builder.setFooter(ld.getDayOfMonth() + "/" + ld.getMonthValue() + "/" + ld.getYear() + " (day/month/year)");

        staffchannel.sendMessageEmbeds(builder.build()).queue();
    }

    public void BotisOfflineembed(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (ServerStatuschannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .addField("Server Restart Schedule (PST)", "1am, 5am, 9am, 1pm, 5pm, 9pm, 1am", true)
                .addField("Time Converter", "https://www.timeanddate.com/worldclock/converter.html", true)
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        ServerStatuschannel.sendMessageEmbeds(builder.build()).queue();
        purgeMessages(ServerStatuschannel);
    }
    public void Serverisoffline(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void ServerisOnline(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.GREEN);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    @Contract(pure = true)
    public @NotNull String convertTime(Long ms) {
        int seconds = (int) (ms / 1000) % 60;
        int minutes = (int) ((ms / (1000 * 60)) % 60);
        int hours = (int) ((ms / (1000 * 60 * 60)) % 24);

        return hours + " hours, " + minutes + " minutes and " + seconds + " seconds";
    }

    public void AFK(){
        AFKManager afkManager = new AFKManager(playerlog);

        //getCommand("isafk").setExecutor(new isAFKCommand(afkManager));
        //getCommand("afk").setExecutor(new AFKCommand(afkManager));

        getServer().getPluginManager().registerEvents(new AFKListener(afkManager), this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new MovementChecker(afkManager), 0L, 600L);
    }

    public void BotSendEmbed(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (ServerStatuschannel != null) {
            EmbedBuilder builder = (new EmbedBuilder()).setAuthor(contentAuthorLine ? content : player.getName(), null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1")
                    .addField("Server Restart Schedule (PST)", "1am, 5am, 9am, 1pm, 5pm, 9pm, 1am", true)
                    .addField("Time Converter", "https://www.timeanddate.com/worldclock/converter.html", true);
            builder.setColor(java.awt.Color.GREEN);
            if (!contentAuthorLine) {
                builder.setDescription(content);
            }
            ServerStatuschannel.sendMessageEmbeds(builder.build(), new MessageEmbed[0]).queue();
            purgeMessages(ServerStatuschannel);
        }
    }

    private void sendMessage(Player player, String content,  boolean contentAuthorLine){
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.GREEN);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void sendadvancementmsg(Player player, String content, boolean contentAuthorLine) {
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.CYAN);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void senddeathmsg(Player player, String content, boolean contentAuthorLine){
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void sendoffmsg(Player player, String content, boolean contentAuthorLine) {
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void SendFirstJoinmsg(Player player, String content, boolean contentAuthorLine){
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.ORANGE);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void SendMsg(Player player, String content, boolean contentAuthorLine) {
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.GRAY);
        if (!contentAuthorLine) {
            builder.setDescription(content);
        }
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public class SpigotListener implements Listener {
        @EventHandler
        public void onFirstJoin(@NotNull PlayerJoinEvent e) {
            Player player = e.getPlayer();
            if (!player.hasPlayedBefore()) {
                SendFirstJoinmsg(player, player.getName() + " has join the game for the first time!", true);
            }
        }
        @EventHandler
        public void onChat(@NotNull AsyncPlayerChatEvent e) {
            Player player = e.getPlayer();
            String message = e.getMessage().toLowerCase();
            SendMsg(player, e.getPlayer().getName() + " >> " + " "  + message , true);
        }
        @EventHandler
        public void onJoin(@NotNull PlayerJoinEvent e){
            Player player = e.getPlayer();
            sendMessage(player, e.getPlayer().getName() + " joined the game.", true);
        }
        @EventHandler
        public void onQuit(@NotNull PlayerQuitEvent e){
            Player player = e.getPlayer();
            sendoffmsg(e.getPlayer(), player.getName() + " left the game.", true);
        }
        @EventHandler
        public void onDeath(@NotNull PlayerDeathEvent e){
            Player p = e.getEntity();
            String deathMessage = e.getDeathMessage() == null ? p.getName() + " died." : e.getDeathMessage();
            senddeathmsg(p, deathMessage, true);
        }
        @EventHandler
        public void onAdvancement(@NotNull PlayerAdvancementDoneEvent e){
            Player player = e.getPlayer();
            String advancementKey  = e.getAdvancement().getKey().getKey();
            String display = advancementToDisplayMap.get(advancementKey);
            if(display == null ) return;
            sendadvancementmsg(player, player.getName() + " has made the advancement ["+ display + "]", true);
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
    @Contract("_ -> new")
    private @NotNull String Color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static String getPREFIX() {
        return PREFIX;
    }

    public HashMap<UUID, Long> getMap() {
        return map;
    }
}