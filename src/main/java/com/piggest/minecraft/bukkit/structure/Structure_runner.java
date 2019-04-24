package com.piggest.minecraft.bukkit.structure;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class Structure_runner extends BukkitRunnable {
	private boolean unload_run = false;

	public boolean unload_run() {
		return this.unload_run;
	}

	public void set_unload_run(boolean unload_run) {
		this.unload_run = unload_run;
	}

	public abstract int get_cycle();

	public abstract int get_delay();

	public abstract boolean is_asynchronously();
}
