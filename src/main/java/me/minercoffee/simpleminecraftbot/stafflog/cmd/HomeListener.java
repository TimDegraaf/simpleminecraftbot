package me.minercoffee.simpleminecraftbot.stafflog.cmd;

import me.minercoffee.simpleminecraftbot.utils.ColorMsg;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static me.minercoffee.simpleminecraftbot.stafflog.cmd.HomeUtils.HomesTPList;
import static me.minercoffee.simpleminecraftbot.stafflog.cmd.HomeUtils.MainMenuGui;
import static me.minercoffee.simpleminecraftbot.stafflog.cmd.Homes.homeName;
import static me.minercoffee.simpleminecraftbot.utils.DataManager.getDataConfig;
import static me.minercoffee.simpleminecraftbot.utils.DataManager.getMessagesConfig;

public class HomeListener implements Listener {
    @EventHandler
    public void clickEvent(@NotNull InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        Configuration config = getMessagesConfig();
        String main_menu_name = ColorMsg.color(config.getString("staffhome.gui-main-menu-title"));
        String second_menu_name = ChatColor.DARK_GRAY + ("Homes TP List");
        Inventory openinv = e.getClickedInventory();
        ItemStack item = e.getCurrentItem();

        if (openinv == null) return;
        if(e.getView().getTitle().equalsIgnoreCase(main_menu_name)){
            if (item == null || !item.hasItemMeta()) return;

            if (item.getType() == Material.MAGENTA_STAINED_GLASS_PANE) {
                MainMenuGui((Player) e.getWhoClicked());
                e.setCancelled(true);
                return;
            }
            if (item.getItemMeta().hasDisplayName()) {

                if (item.getType().equals(Material.BEDROCK)) {
                    p.closeInventory();
                    e.setCancelled(true);
                }
                if (item.getType().equals(Material.BLACK_BED)) {
                    HomesTPList(p);
                    e.setCancelled(true);

                }
            }
            e.setCancelled(true);
        }
        if (e.getView().getTitle().equalsIgnoreCase(second_menu_name)) {
            if (item == null || !item.hasItemMeta()) return;
            if (item.hasItemMeta()) {
                if (item.getType() == Material.MAGENTA_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    HomesTPList((Player) e.getWhoClicked());
                    return;
                }
                if (item.getItemMeta().hasDisplayName()) {

                    if (item.getType().equals(Material.RED_BED)) {
                        homeTP(p);
                        e.setCancelled(true);
                    }
                    if (item.getType().equals(Material.BARRIER)) {
                        MainMenuGui(p);
                        e.setCancelled(true);
                    }
                    if (item.getType().equals(Material.BEDROCK)) {
                        p.closeInventory();
                        e.setCancelled(true);
                    }

                }
                e.setCancelled(true);
            }

        }
    }

    public static void  homeTP(Player p){
        Configuration data = getDataConfig();
        for(String key : getDataConfig().getConfigurationSection("staff-homes-locations").getKeys(false)) {

            int x = getDataConfig().getInt(key + homeName + ".x");
            int y = getDataConfig().getInt(key + homeName + ".y");
            int z = getDataConfig().getInt(key + homeName + ".z");
            String world = getDataConfig().getString(key + homeName +  ".worldName");
            if (world != null && Bukkit.getWorld(world) != null) {
                System.out.println("testing location");
                World w = Bukkit.getWorld(world);
                Location loc = new Location(w, x, y, z);
                p.teleport(loc);
                p.sendMessage(ChatColor.GREEN + "Teleporting to " + data.getString(key + homeName + ".homename") + " in the " + data.getString(key + homeName + ".worldName") + " " + "@: " + ChatColor.GRAY + data.getInt(key + homeName + ".x") + " " + data.getInt(key + homeName + ".y") + " " + data.getInt(key + homeName + ".z") + " " + data.getInt(key + homeName + ".pinch") + " " + data.getInt(key + homeName + ".yaw"));

            } else {
                System.out.println("no world found.");
            }
        }
    }
}
