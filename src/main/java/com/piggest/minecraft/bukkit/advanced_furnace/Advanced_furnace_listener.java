package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Advanced_furnace_listener implements Listener {
	@EventHandler
	public void on_look(PlayerInteractEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block break_block = event.getClickedBlock();
			Advanced_furnace adv_furnace = Dropper_shop_plugin.instance.get_adv_furnace_manager()
					.find(break_block.getLocation(), false);
			if (adv_furnace != null) {
				if (player.isSneaking() == false) {
					player.closeInventory();
					player.openInventory(adv_furnace.getInventory());
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void on_use_gas_bottle(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			ItemStack item = event.getItem();
			if (Gas_bottle.is_gas_bottle(item)) {
				event.getPlayer().sendMessage("使用了气体瓶");
				Gas_bottle.clean_contents(item);
				Gas_bottle.set_contents(item, Gas.oxygen, 210);
				Gas_bottle.set_contents(item, Gas.nitrogen, 790);
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		if (event.getClickedInventory() == null) {
			return;
		}
		if (event.getClickedInventory().getName().equals("高级熔炉")) {
			int slot = event.getSlot();
			if (slot >= 0 && slot <= 8 || slot == 10 || slot == 12 || slot == 14 || slot == 16 || slot == 19
					|| slot == 21 || slot == 23 || slot == 25 || slot == 26) {
				if (slot == 2 || slot == 5) {
					ItemStack item = event.getCurrentItem();
					ItemMeta meta = item.getItemMeta();
					List<String> lore = meta.getLore();
					String info = lore.get(0);
					if (info.equals("§r开启")) {
						lore.set(0, "§r关闭");
					} else {
						lore.set(0, "§r开启");
					}
					meta.setLore(lore);
					item.setItemMeta(meta);
				} else if (slot == 3 || slot == 4 || slot == 6) {
					ItemStack item = event.getCurrentItem();
					ItemMeta meta = item.getItemMeta();
					List<String> lore = new ArrayList<String>();
					lore.add("wait...");
					meta.setLore(lore);
					item.setItemMeta(meta);
				}
				event.setCancelled(true);
			}
		}
	}
}
