package com.piggest.minecraft.bukkit.teleport_machine.dynmap;

import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine;
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine_manager;

public class Dynmap_refresher extends Structure_runner {

	private Teleport_machine teleport_machine;

	public Dynmap_refresher(Teleport_machine teleport_machine) {
		this.teleport_machine = teleport_machine;
	}

	@Override
	public void run() {
		Teleport_machine_manager manager = (Teleport_machine_manager) this.teleport_machine.get_manager();
		Dynmap_manager dynmap_manager = manager.get_dynmap_manager();
		dynmap_manager.handle_teleport_machine_update(this.teleport_machine);
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
