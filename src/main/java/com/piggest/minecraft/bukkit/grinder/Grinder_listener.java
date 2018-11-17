package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Grinder_listener implements Listener {
	@EventHandler
	public void on_look(PlayerInteractEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block clicked_block = event.getClickedBlock();
			Grinder grinder = Dropper_shop_plugin.instance.get_grinder_manager().get(clicked_block.getLocation());
			if (grinder != null) {
				if (player.isSneaking() == false) {
					Inventory gui = grinder.getInventory();
					player.closeInventory();
					player.openInventory(gui);
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
		if (event.getClickedInventory().getName().equals("磨粉机")) {
			event.getWhoClicked().sendMessage("编号" + event.getSlot());
			int slot = event.getSlot();
			if (slot >= 0 && slot <= 8 || slot == 10 || slot == 12 || slot == 14) {
				event.setCancelled(true);
			}
		}
	}
}
