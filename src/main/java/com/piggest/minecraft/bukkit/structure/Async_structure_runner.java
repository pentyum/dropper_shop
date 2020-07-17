package com.piggest.minecraft.bukkit.structure;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Collection;

public abstract class Async_structure_runner extends Thread implements Structure_runner {
	Structure_manager<? extends Structure> manager;

	public Async_structure_runner(Structure_manager<? extends Structure> manager) {
		this.manager = manager;
		this.setName(this.manager.get_permission_head() + ":" + this.getClass().getSimpleName());
	}

	private boolean unload_run = false;

	public boolean unload_run() {
		return this.unload_run;
	}

	public void set_unload_run(boolean unload_run) {
		this.unload_run = unload_run;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(this.get_delay() * 50);
		} catch (InterruptedException e) {
			return;
		}
		while (!this.isInterrupted()) {
			long start_time = System.currentTimeMillis();
			for (World world : Bukkit.getWorlds()) {
				Collection<? extends Structure> structures = manager.get_all_structures_in_world(world);
				for (Structure structure : structures) {
					this.run_instance(structure);
				}
			}
			int max_time = this.get_cycle() * 50;
			try {
				long sleep_time = max_time - (System.currentTimeMillis() - start_time);
				if (sleep_time > 0) {
					Thread.sleep(sleep_time);
				} else {
					Dropper_shop_plugin.instance.getLogger().warning("线程执行超时" + (-sleep_time) + "ms");
				}
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public abstract boolean run_instance(Structure structure);

}
