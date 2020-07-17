package com.piggest.minecraft.bukkit.teleport_machine;

import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Old_structure_runner;

public class Auto_refresher extends Old_structure_runner {
	public Auto_refresher(Teleport_machine_manager manager) {
		super(manager);
	}

	@Override
	public void run_instance(Structure structure) {
		Teleport_machine terminal = (Teleport_machine) structure;
		terminal.set_gui_terminal_list(terminal.get_current_page());
	}

	@Override
	public int get_cycle() {
		return 100;
	}

	@Override
	public int get_delay() {
		return 20;
	}

	@Override
	public boolean is_asynchronously() {
		return true;
	}
}
