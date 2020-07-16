package com.piggest.minecraft.bukkit.teleport_machine.dynmap;

import com.piggest.minecraft.bukkit.structure.Structure_manager_runner;
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine;
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine_manager;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class Dynmap_refresher extends Structure_manager_runner {

	private Teleport_machine_manager manager;

	public Dynmap_refresher(Teleport_machine_manager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		Dynmap_manager dynmap_manager = manager.get_dynmap_manager();
		for (World world : Bukkit.getServer().getWorlds()) {
			for (Teleport_machine machine : this.manager.get_all_structures_in_world(world)) {
				dynmap_manager.handle_teleport_machine_update(machine);
			}
		}

	}

	@Override
	public int get_cycle() {
		return 1200;
	}

	@Override
	public int get_delay() {
		return 200;
	}

	@Override
	public boolean is_asynchronously() {
		return true;
	}

}
