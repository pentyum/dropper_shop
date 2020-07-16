package com.piggest.minecraft.bukkit.pigman_switch;

import com.piggest.minecraft.bukkit.utils.Chunk_location;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.HashSet;

public class Pigman_spawn_listener implements Listener {
	@EventHandler
	public void on_spawn(EntitySpawnEvent event) {
		EntityType type = event.getEntityType();
		if (type == EntityType.ZOMBIFIED_PIGLIN) {
			Chunk spawn_chunk = event.getLocation().getChunk();
			Chunk_location chunk_location = new Chunk_location(spawn_chunk);
			HashSet<Pigman_switch> find = Pigman_switch_manager.instance.get_all_structures_around_chunk(chunk_location,
					1);
			if (find != null) {
				for (Pigman_switch pigman_switch : find) {
					if (pigman_switch.activated() == true) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
