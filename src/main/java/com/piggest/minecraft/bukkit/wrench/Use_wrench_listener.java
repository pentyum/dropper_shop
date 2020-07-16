package com.piggest.minecraft.bukkit.wrench;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_manager;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.block.data.type.Piston;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Use_wrench_listener implements Listener {
	private Wrench_command_executor wrench_plugin = null;

	public Use_wrench_listener(Wrench_command_executor wrench_plugin) {
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
		if (event.useItemInHand() != Result.DENY && event.useInteractedBlock() != Result.DENY
				&& event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.hasItem() == true && event.getBlockFace() != null) {
				ItemStack wrench_item = event.getItem();
				if (!wrench_plugin.is_wrench(wrench_item)) {
					return;
				}
				Player player = event.getPlayer();
				if (!player.hasPermission("wrench.use")) {
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
					event.setCancelled(true);
				} else {
					HashMap<Class<? extends Structure>, Structure_manager<? extends Structure>> structure_manager = Dropper_shop_plugin.instance
							.get_structure_manager();
					for (Entry<Class<? extends Structure>, Structure_manager<? extends Structure>> entry : structure_manager
							.entrySet()) {
						Structure_manager<? extends Structure> manager = entry.getValue();
						Structure structure = manager.find_existed(block.getLocation());
						// Structure structure = manager.find(null, block.getLocation(), false);
						if (structure != null && player.isSneaking() == false) {
							player.sendMessage("这里已经有结构了");
							event.setCancelled(true);
							return;
						}
					}
					for (Entry<Class<? extends Structure>, Structure_manager<? extends Structure>> entry : structure_manager
							.entrySet()) {
						Structure_manager<? extends Structure> manager = entry.getValue();
						Structure structure = manager.find_and_make(player, block.getLocation());
						if (structure != null && player.isSneaking() == false) {
							String permission = manager.get_permission_head() + ".make";
							if (!player.hasPermission(permission)) {
								player.sendMessage("你没有建立" + structure.get_display_name() + "结构的权限!");
								event.setCancelled(true);
								return;
							}
							if (structure instanceof Multi_block_structure && structure.create_condition(player)) {
								manager.add(structure);
								player.sendMessage(structure.get_display_name() + "结构建立完成");
								event.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}
	}
}
