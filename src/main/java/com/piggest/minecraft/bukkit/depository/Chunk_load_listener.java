package com.piggest.minecraft.bukkit.depository;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class Chunk_load_listener implements Listener {
	@EventHandler
	void on_chunk_load(ChunkLoadEvent event) {
		//int x = event.getChunk().getX();
		//int z = event.getChunk().getZ();
		//Dropper_shop_plugin.instance.getLogger().info("区块("+x+","+z+")已加载");
	}
	
	@EventHandler
	void on_chunk_unload(ChunkUnloadEvent event) {
		//int x = event.getChunk().getX();
		//int z = event.getChunk().getZ();
		//Dropper_shop_plugin.instance.getLogger().info("区块("+x+","+z+")已卸载");
	}
}