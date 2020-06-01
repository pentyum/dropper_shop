package com.piggest.minecraft.bukkit.structure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;

public class Structure_listener implements Listener {
	@EventHandler
	public void on_explode(BlockExplodeEvent event) {
		if (event.isCancelled() == true) {
			return;
		}

	}

	@EventHandler
	public void on_break(BlockBreakEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Location loc = block.getLocation();
		HashMap<Class<? extends Structure>, Structure_manager<? extends Structure>> structure_manager = Dropper_shop_plugin.instance
				.get_structure_manager();
		for (Entry<Class<? extends Structure>, Structure_manager<? extends Structure>> entry : structure_manager
				.entrySet()) {
			Structure_manager<? extends Structure> manager = entry.getValue();
			Structure structure = manager.find_existed(loc);
			if (structure != null) { // 附近有结构
				if (structure instanceof Multi_block_structure) {
					Multi_block_structure multi_block_structure = (Multi_block_structure) structure;
					if (!multi_block_structure.in_structure(loc)) {
						return;
					}
				}
				if (structure.on_break(player) == false) {
					event.setCancelled(true);
				} else {
					ItemStack[] drop_items = structure.get_drop_items();
					if (drop_items != null) {
						for (ItemStack item : drop_items) {
							if (!Grinder.is_empty(item)) {
								loc.getWorld().dropItem(loc, item);
							}
						}
					}
					player.sendMessage(structure.getClass().getSimpleName() + "结构已被移除");
					structure.remove();
				}
			}
		}
	}

	@EventHandler
	public void on_right_click(PlayerInteractEvent event) {
		if (event.useItemInHand() == Result.DENY || event.useInteractedBlock() == Result.DENY) {
			return;
		}
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			HashMap<Class<? extends Structure>, Structure_manager<? extends Structure>> structure_manager = Dropper_shop_plugin.instance
					.get_structure_manager();
			for (Entry<Class<? extends Structure>, Structure_manager<? extends Structure>> entry : structure_manager
					.entrySet()) {
				Structure_manager<? extends Structure> manager = entry.getValue();
				Structure structure = manager.find_existed(block.getLocation());
				if (structure != null && player.isSneaking() == false) {
					if (structure instanceof Multi_block_structure) {
						Multi_block_structure multi_block_structure = (Multi_block_structure) structure;
						if (multi_block_structure.in_structure(block.getLocation())) {
							multi_block_structure.on_right_click(player);
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void on_world_load(WorldLoadEvent event) {
		Collection<Structure_manager<? extends Structure>> managers = Dropper_shop_plugin.instance.get_structure_manager().values();
		for (Structure_manager<? extends Structure> manager : managers) {
			manager.load_config_from_world(event.getWorld());
			manager.load_instance_from_world_config(event.getWorld());
			manager.load_world_structures(event.getWorld());
		}
	}
}
