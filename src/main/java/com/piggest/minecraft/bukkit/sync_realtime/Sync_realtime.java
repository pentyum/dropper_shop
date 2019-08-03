package com.piggest.minecraft.bukkit.sync_realtime;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.utils.Server_date;

public class Sync_realtime extends BukkitRunnable {
	private List<String> sync_realtime_worlds;

	public Sync_realtime(List<String> sync_realtime_worlds) {
		this.sync_realtime_worlds = sync_realtime_worlds;
	}

	@Override
	public void run() {
		for (String world_name : sync_realtime_worlds) {
			World world = Bukkit.getWorld(world_name);
			if (world == null) {
				continue;
			}
			world.setFullTime(Server_date.real_time_to_full_time());
		}
	}

}
