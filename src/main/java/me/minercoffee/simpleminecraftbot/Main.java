package me.minercoffee.simpleminecraftbot;

import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.jeff_media.updatechecker.UserAgentBuilder;
import me.minercoffee.simpleminecraftbot.clearcmd.Clear;
import me.minercoffee.simpleminecraftbot.stafflog.cmd.*;
import me.minercoffee.simpleminecraftbot.stafflog.listeners.*;
import me.minercoffee.simpleminecraftbot.stafflog.staffchat;
import me.minercoffee.simpleminecraftbot.ticket.ButtonListener;
import me.minercoffee.simpleminecraftbot.ticket.SetTicketCMD;
import me.minercoffee.simpleminecraftbot.utils.*;
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
import org.jetbrains.annotations.Nullable;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static me.minercoffee.simpleminecraftbot.utils.DataManager.*;

public final class Main extends JavaPlugin {
    //TODO
    // Fixed staff time from not saving while quiting, joining, or restarting
    //TODO
    // Add custom Messages in ServerStatus fields
    public PlayerLogListener playerLogListener;
    public static HashMap<UUID, Long> map = new HashMap<>();
    public DataManager data;
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
    int i = 1;
    private static final String PREFIX = "!";
    public static Main getInstance() {
        return instance;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onEnable() {
        super.onEnable();
        this.playerLogListener = new PlayerLogListener(this);
        setInstance(this);
        saveDefaultConfig();
        saveConfig();
        loadConfig();
        ServerUtils();
        DataManager.StartupAdvancementConfig();
        StaffplayimeUpdater();
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
            String ingamestaffchatid = getConfig().getString("staffchat");
            if (ingamestaffchatid != null){
                ingamestaffchatchannel = jda.getTextChannelById(ingamestaffchatid);
            }
            String ServerStatusid = getConfig().getString("serverstatus");
            if (ServerStatusid != null){
                System.out.println("testa");
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
            new staffchat(this);
            jda.addEventListener(new Discordhelp(this), new DiscordBotPingEvent(this),  new staffchat(this));
            getCommand("staffcheck").setExecutor(new CommandCheck(this));
            getCommand("staffplaytime").setExecutor(new Staffplaytime(this));
            getCommand("staff").setExecutor(new PlayerLogListener(instance));
            getServer().getPluginManager().registerEvents(new PlayerLogListener(instance), this);
            getServer().getPluginManager().registerEvents(new UpdateCheckListener(this), this);
            new DateCheckRunnable(this).runTaskTimerAsynchronously(this, 0L, 60L * 20L);
            new PlayerSaveTask().runTaskTimerAsynchronously(this, 0L, 60 * 20L);
            new DailySummaryTask(this).runTaskTimerAsynchronously(this, 0L, 60L * 20L);
            jda.addEventListener(new Clear(this), new BotCommands(), new ModalListeners());
            jda.addEventListener(new ButtonListener(this));
            jda.addEventListener(new SetTicketCMD());
            jda.addEventListener(new DiscordListener());
            jda.addEventListener(new OnlineStaff(this));
            jda.addEventListener(new DiscordCommandCheckLegacy(this));
            getCommand("ping").setExecutor(new Ping());
            new reloadcmd(this);
            getServer().getPluginManager().registerEvents(new SpigotListener(), this);
            getCommand("updatechecker").setExecutor(new UpdateCheckCommand());
            getCommand("sbreload").setExecutor(new reloadcmd(this));
            this.data = new DataManager(this);
            this.ServerRestartingOn(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is online.", true, Color.GREEN);
            ServerisOnline(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is online.", true, Color.RED);
            //advancement config getting the names.
            ConfigurationSection advancementMap = getadvancementsConfig().getConfigurationSection("advancementMap");
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
        instance.saveConfig();
    }
    public void StaffplayimeUpdater(){
        getadvancementsConfig().addDefault("staffplaytime", true);
        savestaffplaytime();
    }
    public void AdvancementsUpdater(){
        getadvancementsConfig().addDefault("advancementMap", true);
        getadvancementsConfig().addDefault("advancementMap.story/mine_stone", "Stone Age");
        getadvancementsConfig().addDefault("advancementMap.story/upgrade_tools", "Getting an Upgrade");
        getadvancementsConfig().addDefault("advancementMap.story/smelt_iron", "Acquire Hardware");
        getadvancementsConfig().addDefault("advancementMap.story/obtain_armor", "Suit Up");
        getadvancementsConfig().addDefault("advancementMap.story/lava_bucket", "Hot Stuff");
        getadvancementsConfig().addDefault("advancementMap.story/iron_tools", "Isn't It Iron Pick");
        getadvancementsConfig().addDefault("advancementMap.story/deflect_arrow", "Not Today, Thank You");
        getadvancementsConfig().addDefault("advancementMap.story/form_obsidian", "Ice Bucket Challenge");
        getadvancementsConfig().addDefault("advancementMap.story/mine_diamond", "Diamonds!");
        getadvancementsConfig().addDefault("advancementMap.story/enter_the_nether", "We Need To Go Deeper");
        getadvancementsConfig().addDefault("advancementMap.story/shiny_gear", "Cover Me With Diamonds");
        getadvancementsConfig().addDefault("advancementMap.story/enchant_item", "Enchanter!");
        getadvancementsConfig().addDefault("advancementMap.story/cure_zombie_villager", "Zombie Doctor!");
        getadvancementsConfig().addDefault("advancementMap.story/follow_ender_eye", "Eye Spy");
        getadvancementsConfig().addDefault("advancementMap.story/enter_the_end", "The End?");
        getadvancementsConfig().addDefault("advancementMap.nether/return_to_sender", "Return to Sender");
        getadvancementsConfig().addDefault("advancementMap.nether/find_bastion", "Those Were the Days");
        getadvancementsConfig().addDefault("advancementMap.nether/obtain_ancient_debris", "Hidden in the Depths");
        getadvancementsConfig().addDefault("advancementMap.nether/fast_travel", "Subspace Bubble");
        getadvancementsConfig().addDefault("advancementMap.nether/find_fortress", "A Terrible Fortress");
        getadvancementsConfig().addDefault("advancementMap.nether/obtain_crying_obsidian", "Who is Cutting Onions");
        getadvancementsConfig().addDefault("advancementMap.nether/distract_piglin", "Oh Shiny!");
        getadvancementsConfig().addDefault("advancementMap.nether/ride_strider", "This Boat Has Legs");
        getadvancementsConfig().addDefault("advancementMap.nether/uneasy_alliance", "Uneasy Alliance");
        getadvancementsConfig().addDefault("advancementMap.nether/loot_bastion", "War Pigs!");
        getadvancementsConfig().addDefault("advancementMap.nether/use_lodestone", "Country Lode, Take Me Home");
        getadvancementsConfig().addDefault("advancementMap.nether/netherite_armor", "Cover Me in Debris!");
        getadvancementsConfig().addDefault("advancementMap.nether/get_wither_skull", "Spooky Scary Skeleton!");
        getadvancementsConfig().addDefault("advancementMap.nether/obtain_blaze_rod", "Into Fire!");
        getadvancementsConfig().addDefault("advancementMap.nether/charge_respawn_anchor", "Not Quite Nine Lives");
        getadvancementsConfig().addDefault("advancementMap.nether/explore_nether", "Hot Tourist Destinations!");
        getadvancementsConfig().addDefault("advancementMap.nether/summon_wither", "Withering Heights");
        getadvancementsConfig().addDefault("advancementMap.nether/brew_potion", "Bring Home the Beacon");
        getadvancementsConfig().addDefault("advancementMap.nether/create_beacon", "Bring Home the Beacon");
        getadvancementsConfig().addDefault("advancementMap.nether/all_potions", "A Furious Cocktail");
        getadvancementsConfig().addDefault("advancementMap.nether/create_full_beacon", "Beaconator");
        getadvancementsConfig().addDefault("advancementMap.nether/all_effects", "How Did We Get Here?");
        getadvancementsConfig().addDefault("advancementMap.end/kill_dragon", "Free the End");
        getadvancementsConfig().addDefault("advancementMap.end/dragon_egg", "The Next Generation");
        getadvancementsConfig().addDefault("advancementMap.end/enter_end_gateway", "Remote Getaway");
        getadvancementsConfig().addDefault("advancementMap.end/respawn_dragon", "The End... Again...");
        getadvancementsConfig().addDefault("advancementMap.end/dragon_breath",  "You Need a Mint");
        getadvancementsConfig().addDefault("advancementMap.end/find_end_city", "The City at the End of the Game");
        getadvancementsConfig().addDefault("advancementMap.end/elytra", "Sky's the Limit");
        getadvancementsConfig().addDefault("advancementMap.end/levitate", "Great View From Up Here");
        getadvancementsConfig().addDefault("advancementMap.adventure/voluntary_exile", "Voluntary Exile");
        getadvancementsConfig().addDefault("advancementMap.adventure/kill_a_mob", "Monster Hunter");
        getadvancementsConfig().addDefault("advancementMap.adventure/trade", "What a Deal!");
        getadvancementsConfig().addDefault("advancementMap.adventure/honey_block_slide", "Sticky Situation");
        getadvancementsConfig().addDefault("advancementMap.adventure/ol_betsy", "Ol' Betsy");
        getadvancementsConfig().addDefault("advancementMap.adventure/sleep_in_bed", "Sweet Dreams");
        getadvancementsConfig().addDefault("advancementMap.adventure/hero_of_the_village", "Hero of the Village");
        getadvancementsConfig().addDefault("advancementMap.adventure/throw_tident", "Throwaway Joke");
        getadvancementsConfig().addDefault("advancementMap.adventure/shoot_arrow", "Take Aim");
        getadvancementsConfig().addDefault("advancementMap.adventure/kill_all_mobs", "Monsters Hunter");
        getadvancementsConfig().addDefault("advancementMap.adventure/two_birds_one_arrow", "Two Birds, One Arrow");
        getadvancementsConfig().addDefault("advancementMap.adventure/totem_of_undying", "Post mortal");
        getadvancementsConfig().addDefault("advancementMap.adventure/summon_iron_golem", "Hired Help");
        getadvancementsConfig().addDefault("advancementMap.adventure/whos_the_pillager_now", "Who's the Pillager Now?");
        getadvancementsConfig().addDefault("advancementMap.adventure/arbalistic", "Arbalistic");
        getadvancementsConfig().addDefault("advancementMap.adventure/adventuring_time", "Adventuring Time");
        getadvancementsConfig().addDefault("advancementMap.adventure/very_very_frightening", "Very Very Frightening");
        getadvancementsConfig().addDefault("advancementMap.adventure/sniper_duel", "Sniper Duel");
        getadvancementsConfig().addDefault("advancementMap.adventure/bullseye", "Bulls eye");
        getadvancementsConfig().addDefault("advancementMap.husbandry/safely_harvest_honey", "Bee Our Guest");
        getadvancementsConfig().addDefault("advancementMap.husbandry/breed_an_animal", "The Parrots and the Bats");
        getadvancementsConfig().addDefault("advancementMap.husbandry/fishy_business", "Fishy Business");
        getadvancementsConfig().addDefault("advancementMap.husbandry/silk_touch_nest", "Total Beelocation");
        getadvancementsConfig().addDefault("advancementMap.husbandry/plant_seed", "A Seedy Place");
        getadvancementsConfig().addDefault("advancementMap.husbandry/breed_all_animals", "Two by Two");
        getadvancementsConfig().addDefault("advancementMap.husbandry/complete_catalogue", "A Complete Catalogue");
        getadvancementsConfig().addDefault("advancementMap.husbandry/tactical_fishing", "Tactial Fishing");
        getadvancementsConfig().addDefault("advancementMap.husbandry/balanced_diet", "A Balanced Diet");
        getadvancementsConfig().addDefault("advancementMap.husbandry/obtain_netherite_hoe", "Serious Dedication");
        getadvancementsConfig().addDefault("advancementMap.husbandry/allay_deliver_cake_to_note_block", "Birthday Song");
        getadvancementsConfig().addDefault("advancementMap.husbandry/tadpole_in_a_bucket", "Bukkit Bukkit");
        getadvancementsConfig().addDefault("advancementMap.adventure/kill_mob_near_sculk_catalyst", "It Spreads");
        getadvancementsConfig().addDefault("advancementMap.adventure/avoid_vibration", "Sneak 100");
        getadvancementsConfig().addDefault("advancementMap.husbandry/leash_all_frog_variants", "When the Squad Hops into Town");
        getadvancementsConfig().addDefault("advancementMap.husbandry/froglights", "With Our Powers Combined");
        getadvancementsConfig().addDefault("advancementMap.husbandry/allay_deliver_item_to_player", "You've Got a Friend in Me");
    }
    public void loadConfig(){
        getConfig().options().copyDefaults(true);
        instance.saveConfig();
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onDisable() {
        (new PlayerLogListener(instance)).saveAllPlayers();
        try {
            ServerRestartingOff(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is offline.", true, Color.RED);
            Serverisoffline(Bukkit.getOfflinePlayer("MinerCoffee97"), "Server is Restarting.", true, Color.RED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void sendstaffonline(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (staffplaytimechannel == null) return;
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

        staffplaytimechannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void senddtaffoffline(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (staffplaytimechannel == null) return;
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

        staffplaytimechannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void sendStaffCurrentTime(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if (staffplaytimechannel == null) return;

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

        staffplaytimechannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void sendStaffChatEmbled(OfflinePlayer player, String content, Color ignoredColor) {
        if (ingamestaffchatchannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.YELLOW);
        ingamestaffchatchannel.sendMessageEmbeds(builder.build()).queue();
    }
    public void ServerRestartingOff(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if(this.getConfig().getBoolean("Status-enable")) return;
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
    public void ServerRestartingOn(OfflinePlayer player, String content, boolean contentAuthorLine, Color ignoredColor) {
        if(this.getConfig().getBoolean("Status-enable")) return;
        EmbedBuilder builder = new EmbedBuilder()
                .addField("Server Restart Schedule (PST)", "1am, 5am, 9am, 1pm, 5pm, 9pm, 1am", true)
                .addField("Time Converter", "https://www.timeanddate.com/worldclock/converter.html", true)
                .setAuthor(contentAuthorLine ? content : player.getName(),
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.GREEN);
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

    private @Nullable String mcplayercounter(){
        ArrayList<Player> list = new ArrayList<>(this.getServer().getOnlinePlayers());
        for (Player player : list) {
            if (player.isOnline()){
                i++;
            }
        }
        return null;
    }

    private void sendMessage(Player player, String content) {
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder().setAuthor(content, null,
                            "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1")
                    .setFooter(mcplayercounter());
            builder.setColor(java.awt.Color.GREEN);
            chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void sendadvancementmsg(Player player, String content) {
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.CYAN);
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void senddeathmsg(Player player, String content){
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void sendoffmsg(Player player, String content) {
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.RED);
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void SendFirstJoinmsg(Player player, String content){
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.ORANGE);
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    private void SendMsg(Player player, String content) {
        if (chatChannel == null) return;
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(content,
                        null,
                        "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");
        builder.setColor(java.awt.Color.GRAY);
        chatChannel.sendMessageEmbeds(builder.build()).queue();
    }
    public class SpigotListener implements Listener {
        @EventHandler
        public void onFirstJoin(@NotNull PlayerJoinEvent e) {
            Player player = e.getPlayer();
            if (!player.hasPlayedBefore()) {
                SendFirstJoinmsg(player, player.getName() + " has join the game for the first time!");
            }
        }
        @EventHandler
        public void onChat(@NotNull AsyncPlayerChatEvent e) {
            Player player = e.getPlayer();
            String message = e.getMessage().toLowerCase();
            SendMsg(player, e.getPlayer().getName() + " >> " + " "  + message);
        }
        @EventHandler
        public void onJoin(@NotNull PlayerJoinEvent e){
            Player player = e.getPlayer();
            sendMessage(player, e.getPlayer().getName() + " joined the game.");
        }
        @EventHandler
        public void onQuit(@NotNull PlayerQuitEvent e){
            Player player = e.getPlayer();
            sendoffmsg(e.getPlayer(), player.getName() + " left the game.");
        }
        @EventHandler
        public void onDeath(@NotNull PlayerDeathEvent e){
            Player p = e.getEntity();
            String deathMessage = e.getDeathMessage() == null ? p.getName() + " died." : e.getDeathMessage();
            senddeathmsg(p, deathMessage);
        }
        @EventHandler
        public void onAdvancement(@NotNull PlayerAdvancementDoneEvent e){
            Player player = e.getPlayer();
            String advancementKey  = e.getAdvancement().getKey().getKey();
            String display = advancementToDisplayMap.get(advancementKey);
            if (display == null ) return;
            sendadvancementmsg(player, player.getName() + " has made the advancement ["+ display + "]");
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
    public static String getPREFIX() {
        return PREFIX;
    }

    public static HashMap<UUID, Long> getMap() {
        return map;
    }
}