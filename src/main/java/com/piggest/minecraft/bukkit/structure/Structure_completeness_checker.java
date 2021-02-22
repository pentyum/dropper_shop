package com.piggest.minecraft.bukkit.structure;

public class Structure_completeness_checker extends Structure_manager_runner {
	private final Structure_manager<? extends Structure> structure_manager;

	public Structure_completeness_checker(Structure_manager<? extends Structure> structure_manager) {
		this.structure_manager = structure_manager;
	}

	@Override
	public void run() {
		int remove_num = structure_manager.check_structures_completeness();
	}

	@Override
	public int get_cycle() {
		return 200;
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
