package com.piggest.minecraft.bukkit.depository;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.piggest.minecraft.bukkit.Structure.Structure_manager;

public class Depository_listener implements Listener {
	@EventHandler
	public void on_break_depository(BlockBreakEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Block break_block = event.getBlock();
		Depository depository = Structure_manager.plugin.get_depository_manager().find(null, break_block.getLocation(),
				false);
		if (depository != null) {
			event.getPlayer().sendMessage("使用/depository remove移除存储器后才能破坏存储器方块");
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void on_look(PlayerInteractEvent event) {
		if (event.isCancelled() == false && (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
			Block break_block = event.getClickedBlock();
			Depository depository = Structure_manager.plugin.get_depository_manager().find(null, break_block.getLocation(),
					false);
			if (depository != null) {
				event.getPlayer().sendMessage(depository.get_info());
			}
		}
	}
}
