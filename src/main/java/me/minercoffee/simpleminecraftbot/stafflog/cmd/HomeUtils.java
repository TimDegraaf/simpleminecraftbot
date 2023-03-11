package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.utils.ColorMsg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;

import static me.minercoffee.simpleminecraftbot.utils.DataManager.getDataConfig;
import static me.minercoffee.simpleminecraftbot.utils.DataManager.getMessagesConfig;

public class HomeUtils {
    public static String homeName = Homes.homeName;

    public static void MainMenuGui(Player p) {
        Configuration config = getMessagesConfig();
        int main_menu_size = config.getInt("staffhome.gui-main-menu-size");
        String main_menu_name = ColorMsg.color(config.getString("staffhome.gui-main-menu-title"));
        Inventory first_menu = Bukkit.createInventory(p, main_menu_size, main_menu_name);

        ItemStack close = new ItemStack(Material.BEDROCK);
        ItemStack list = new ItemStack(Material.BLACK_BED);

        ItemMeta listmeta = list.getItemMeta();
        listmeta.setDisplayName(ChatColor.GREEN + "Homes list!");
        list.setItemMeta(listmeta);

        ItemMeta close_meta = close.getItemMeta();
        close_meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close!");
        close.setItemMeta(close_meta);
        ItemStack fill = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta meta = fill.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(" ");
            fill.setItemMeta(meta);
        }
        for (int i = 0; i < first_menu.getSize(); i++) {
            first_menu.setItem(i, fill);
        }
        first_menu.setItem(8, close);
        first_menu.setItem(4, list);

        p.openInventory(first_menu);
    }

    public static @NotNull String HomesTPList(Player p) {
        String second_menu_name = ChatColor.DARK_GRAY + ("Homes TP List");
        Inventory second_menu = Bukkit.createInventory(p, InventoryType.BARREL, second_menu_name);
        ItemStack go_back = new ItemStack(Material.BARRIER);
        ItemMeta go_back_meta = go_back.getItemMeta();
        go_back_meta.setDisplayName(ChatColor.GRAY + "Go back");
        go_back.setItemMeta(go_back_meta);
        ItemStack close = new ItemStack(Material.BEDROCK);
        ItemMeta close_meta = close.getItemMeta();
        close_meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close!");
        close.setItemMeta(close_meta);
        for (String key : getDataConfig().getConfigurationSection("staff-homes-locations").getKeys(false)) {
            System.out.println("testing home key");
            String homename = getDataConfig().getString(key + homeName + ".homename");
            ItemStack homes = new ItemStack(Material.RED_BED);
            ItemMeta homes_meta = homes.getItemMeta();
            ArrayList<String> homes_lore = new ArrayList<>();
            homes_lore.add(ChatColor.DARK_RED + "click here to teleport to your home.");
            homes_lore.add(ChatColor.GREEN + "Home:" + " " + homename);
            homes_meta.setLore(homes_lore);
            homes_meta.setDisplayName(ChatColor.GOLD + "Homes");
            homes.setItemMeta(homes_meta);
            second_menu.setItem(1, homes);
            second_menu.setItem(2, homes);
            second_menu.setItem(3, homes);
            second_menu.setItem(4, homes);
            }
        ItemStack fill = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta meta = fill.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(" ");
            fill.setItemMeta(meta);
        }
        for (int i = 0; i < second_menu.getSize(); i++) {
            second_menu.setItem(i, fill);
        }
        second_menu.setItem(0, go_back);
        second_menu.setItem(8, close);
        p.openInventory(second_menu);
        return second_menu_name;
    }
}