package com.piggest.minecraft.bukkit.structure;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Collection;

public abstract class Sync_structure_runner<T extends Structure> extends Structure_runner<T> {
	Structure_manager<T> manager;

	public Sync_structure_runner(Structure_manager<T> manager) {
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
		this.task = Bukkit.getScheduler().runTaskTimer(Dropper_shop_plugin.instance, this, this.get_delay(), this.get_cycle());
	}

	@Override
	public void run() {
		for (World world : Bukkit.getWorlds()) {
			Collection<T> structures = manager.get_all_structures_in_world(world);
			for (T structure : structures) {
				this.run_instance(structure);
			}
		}
	}

}
