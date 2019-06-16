package com.piggest.minecraft.bukkit.trees_felling_machine;

import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Trees_felling_machine_runner extends Structure_runner {
	private Trees_felling_machine machine = null;

	public Trees_felling_machine_runner(Trees_felling_machine machine) {
		this.machine = machine;
	}

	@Override
	public void run() {
		this.machine.do_next();
	}

	@Override
	public int get_cycle() {
		return 10;
	}

	@Override
	public int get_delay() {
		return 10;
	}

	@Override
	public boolean is_asynchronously() {
		return false;
	}

}
