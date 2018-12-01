package com.piggest.minecraft.bukkit.structure;

import org.bukkit.scheduler.BukkitRunnable;

public interface HasRunner {
	public BukkitRunnable[] get_runner();

	public int[] get_runner_cycle();

	public int[] get_runner_delay();
}
