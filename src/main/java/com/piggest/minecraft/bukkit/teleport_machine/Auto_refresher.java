package com.piggest.minecraft.bukkit.teleport_machine;

import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Auto_refresher extends Structure_runner {
	private Teleport_machine terminal;

	public Auto_refresher(Teleport_machine terminal) {
		this.terminal = terminal;
	}
	@Override
	public void run() {
		this.terminal.set_gui_terminal_list(terminal.get_current_page());
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
