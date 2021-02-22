package com.piggest.minecraft.bukkit.structure;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Collection;

public abstract class Async_structure_runner<T extends Structure> extends Structure_runner<T> {
	protected Structure_manager<T> manager;

	public Async_structure_runner(Structure_manager<T> manager) {
		this.manager = manager;
		//this.setName(this.manager.get_permission_head() + ":" + this.getClass().getSimpleName());
	}

	private boolean unload_run = false;

	public boolean unload_run() {
		return this.unload_run;
	}

	public void set_unload_run(boolean unload_run) {
		this.unload_run = unload_run;
	}

	@Override
	public void start() {
		this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(Dropper_shop_plugin.instance, this, this.get_delay(), this.get_cycle());
	}

	@Override
	public void run() {
		long start_time = System.currentTimeMillis();
		for (World world : Bukkit.getWorlds()) {
			Collection<T> structures = manager.get_all_structures_in_world(world);
			for (T structure : structures) {
				synchronized (structure) {
					this.run_instance(structure);
				}
			}
		}
		int max_time = this.get_cycle() * 50;
		long sleep_time = max_time - (System.currentTimeMillis() - start_time);
		if (sleep_time < 0) {
			manager.get_logger().warning(this.getClass().getSimpleName() + "线程执行超时" + (-sleep_time) + "ms");
		}
	}
}
