package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
				event.setCancelled(true);
			}
		}
	}
}
