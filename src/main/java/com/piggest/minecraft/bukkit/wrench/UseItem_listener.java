package com.piggest.minecraft.bukkit.wrench;

import java.util.Set;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class UseItem_listener implements Listener {
	private Wrench_command_executor wrench_plugin = null;

	public UseItem_listener(Wrench_command_executor wrench_plugin) {
		this.wrench_plugin = wrench_plugin;
	}

	private boolean direction_changeable(Block block) {
		BlockData data = block.getBlockData();
		if (!(data instanceof Directional)) {
			return false;
		}
		if (data instanceof EndPortalFrame || data instanceof Bed) {
			return false;
		}
		if (data instanceof Piston) {
			Piston piston = (Piston) data;
			if (piston.isExtended() == true) {
				return false;
			}
		}
		return true;
	}

	@EventHandler
	public void on_use_wrench(PlayerInteractEvent event) {
		if (event.isCancelled() == false && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.hasItem() == true && event.getBlockFace() != null) {
				ItemStack wrench_item = event.getItem();
				if (wrench_item.isSimilar(wrench_plugin.get_wrench_item())) {
					Player player = event.getPlayer();
					if(!player.hasPermission("wrench.use")) {
						player.sendMessage("你没有使用扳手的权限!");
						return;
					}
					Block block = event.getClickedBlock();
					if (direction_changeable(block)) {
						Directional directional_data = (Directional) block.getBlockData();
						Set<BlockFace> available_faces = directional_data.getFaces();
						BlockFace clicked_face = event.getBlockFace();
						if (available_faces.contains(clicked_face.getOppositeFace()) && player.isSneaking() == true) {
							directional_data.setFacing(clicked_face.getOppositeFace());
						} else if (available_faces.contains(clicked_face)) {
							directional_data.setFacing(clicked_face);
						}
						player.sendMessage("已使用扳手");
						if (wrench_plugin.use_eco(player) == true) {
							block.setBlockData(directional_data);
						}
					}
				}
			}
		}
	}
}
