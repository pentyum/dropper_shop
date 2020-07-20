package com.piggest.minecraft.bukkit.teleport_machine;

import com.piggest.minecraft.bukkit.structure.Async_structure_runner;

public class Auto_refresher extends Async_structure_runner<Teleport_machine> {
	public Auto_refresher(Teleport_machine_manager manager) {
		super(manager);
	}

	@Override
	public boolean run_instance(Teleport_machine terminal) {
		if (terminal.is_loaded() == false) {
			return false;
		}
		terminal.set_gui_terminal_list(terminal.get_current_page());
		return true;
	}

	@Override
	public int get_cycle() {
		return 100;
	}

	@Override
	public int get_delay() {
		return 20;
	}

}
