package com.piggest.minecraft.bukkit.sync_realtime;

import com.piggest.minecraft.bukkit.utils.Server_date;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map.Entry;

public class Sync_realtime extends BukkitRunnable {
	private HashMap<String, Integer> sync_realtime_worlds;

	public Sync_realtime(HashMap<String, Integer> sync_realtime_worlds) {
		this.sync_realtime_worlds = sync_realtime_worlds;
	}

	@Override
	public void run() {
		for (Entry<String, Integer> entry : sync_realtime_worlds.entrySet()) {
			String world_name = entry.getKey();
			int offset = entry.getValue();
			World world = Bukkit.getWorld(world_name);
			if (world == null) {
				continue;
			}
			world.setFullTime(Server_date.real_time_to_full_time(offset));
		}
	}

}
