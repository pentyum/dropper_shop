package com.piggest.minecraft.bukkit.anti_thunder;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
/* added for experimental fire prevention */
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

/* added for experimental skeleton trap horse prevention */
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.entity.EntityType;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Chunk_location;

/*class Fire_remover extends BukkitRunnable {
	private Location location;

	public Fire_remover(Location loc) {
		this.location = loc;
	}

	@Override
	public void run() {
		int x;
		int y;
		int z;
		for (x = -2; x <= 2; x++) {
			for (y = -1; y <= 1; y++) {
				for (z = -2; z <= 2; z++) {
					Location check_loc = this.location.clone().add(x, y, z);
					if (check_loc.getBlock().getType() == Material.FIRE) {
						check_loc.getBlock().setType(Material.AIR);
					}
				}
			}
		}
	}
}*/

public class Anti_thunder_listener implements Listener {
	public boolean find_anti_thunder(Location loc, String event_string) {
		Chunk_location chunk_loc = new Chunk_location(loc.getChunk());
		Dropper_shop_plugin.instance.getLogger().info("区块" + chunk_loc + "发生雷击");
		HashSet<Anti_thunder> find = Anti_thunder_manager.instance.get_all_structures_around_chunk(chunk_loc, 1);
		if (find != null) {
			for (Anti_thunder anti_thunder : find) {
				Dropper_shop_plugin.instance.getLogger().info("在雷击周围的3*3区块发现防雷器");
				if (anti_thunder.completed() == false) {
					Dropper_shop_plugin.instance.getLogger()
							.info("区块" + anti_thunder.get_chunk_location() + "的防雷器结构不完整，已经移除");
					anti_thunder.remove();
					continue;
				}
				if (anti_thunder.is_active() == true) {
					anti_thunder.send_msg_to_owner("[防雷器] 已阻止" + chunk_loc + "的" + event_string);
					Dropper_shop_plugin.instance.getLogger().info("已阻止" + event_string);
					return true;
				} else {
					Dropper_shop_plugin.instance.getLogger().info("防雷器未被激活，因此" + event_string + "未被阻止");
					continue;
				}
			}
		}
		return false;
	}

	@EventHandler
	public void on_thunder(LightningStrikeEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Location loc = event.getLightning().getLocation();
		event.setCancelled(this.find_anti_thunder(loc, "雷击"));
	}

	@EventHandler
	public void on_ignition(BlockIgniteEvent event) {
		if (event.getCause() != IgniteCause.LIGHTNING) {
			return;
		}
		if (event.isCancelled() == true) {
			return;
		}
		Location loc = event.getBlock().getLocation();
		event.setCancelled(this.find_anti_thunder(loc, "雷击造成的火灾"));
	}

	@EventHandler
	public void on_spawn(EntitySpawnEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		EntityType type = event.getEntityType();
		if (type == EntityType.SKELETON_HORSE) {
			event.setCancelled(this.find_anti_thunder(event.getLocation(), "骷髅陷阱马"));
		}
	}

	@EventHandler
	public void on_powered(BlockPistonExtendEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Chunk_location chunk_loc = new Chunk_location(event.getBlock().getChunk());
		Anti_thunder structure = (Anti_thunder) Anti_thunder_manager.instance.get(event.getBlock().getLocation());
		if (structure != null) {
			if (structure.get_location().equals(event.getBlock().getLocation())) {
				if (structure.activate(true) == true) {
					structure.send_msg_to_owner("[防雷器]区块" + chunk_loc + "的防雷器已经激活");
				} else {
					structure.send_msg_to_owner("[防雷器]区块" + chunk_loc + "的防雷器激活失败");
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void on_dispowered(BlockPistonRetractEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Chunk_location chunk_loc = new Chunk_location(event.getBlock().getChunk());
		Anti_thunder structure = (Anti_thunder) Anti_thunder_manager.instance.get(event.getBlock().getLocation());
		if (structure != null) {
			if (structure.get_location().equals(event.getBlock().getLocation()) && structure.is_active() == true) {
				if (structure.completed() == true) {
					structure.activate(false);
					structure.send_msg_to_owner("[防雷器]区块" + chunk_loc + "的防雷器已经暂停");
				} else {
					structure.remove();
					structure.send_msg_to_owner("[防雷器]区块" + chunk_loc + "的防雷器结构不完整，已经移除");
				}
			}
		}
	}
}
