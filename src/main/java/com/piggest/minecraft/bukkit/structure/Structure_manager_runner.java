package com.piggest.minecraft.bukkit.structure;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class Structure_manager_runner extends BukkitRunnable {

	@Override
	public abstract void run();

	public abstract int get_cycle();

	public abstract int get_delay();

	public abstract boolean is_asynchronously();
}
