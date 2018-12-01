package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
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
			int slot = event.getSlot();
			if (slot >= 0 && slot <= 8 || slot == 10 || slot == 12 || slot == 14 || slot == 16 || slot == 17) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on_break_grinder(BlockBreakEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Block break_block = event.getBlock();
		if (break_block.getType() != Material.SMOOTH_STONE) {
			return;
		}
		Grinder grinder = Dropper_shop_plugin.instance.get_grinder_manager().get(break_block.getLocation());
		if (grinder == null) {
			return;
		}
		event.getPlayer().sendMessage("磨粉机已被破坏");
		Dropper_shop_plugin.instance.get_grinder_manager().remove(grinder);
	}
}