package me.minercoffee.simpleminecraftbot.utils;

import static me.minercoffee.simpleminecraftbot.utils.DataManager.getadvancementsConfig;

public class Advancements {

    public Advancements() {
    }
    public static void AdvancementsUpdater(){
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
    }
