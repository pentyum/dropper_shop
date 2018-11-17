package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.scheduler.BukkitRunnable;

public class Grinder_io_runner extends BukkitRunnable {
	private Grinder grinder;

	public Grinder_io_runner(Grinder grinder) {
		this.grinder = grinder;
	}

	public void run() {
		Hopper hopper = grinder.get_hopper();
		Chest chest = grinder.get_chest();
		if (hopper != null) {

		}
		if (chest != null) {

		}
	}

}
