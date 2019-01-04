package com.piggest.minecraft.bukkit.structure;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class Structure_runner extends BukkitRunnable {
	public abstract int get_cycle();

	public abstract int get_delay();
}
