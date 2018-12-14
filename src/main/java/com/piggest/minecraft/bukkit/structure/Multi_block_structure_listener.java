package com.piggest.minecraft.bukkit.structure;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Multi_block_structure_listener implements Listener {
	@EventHandler
	public void on_right_click(PlayerInteractEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			Structure_manager<?>[] structure_manager = Dropper_shop_plugin.instance.get_structure_manager();
			for(Structure_manager<?> manager : structure_manager) {
				if(manager instanceof Multi_block_structure_manager) {
					Multi_block_structure_manager multi_block_structure_manager = (Multi_block_structure_manager)manager;
					Multi_block_structure structure = multi_block_structure_manager.find(player.getName(), block.getLocation(), false);
					if(structure != null && player.isSneaking()==false) {
						structure.on_right_click(player);
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
