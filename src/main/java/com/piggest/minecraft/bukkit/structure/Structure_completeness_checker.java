package com.piggest.minecraft.bukkit.structure;

import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool_manager;

public class Structure_completeness_checker extends Structure_manager_runner {
	private final Structure_manager<? extends Structure> structure_manager;

	public Structure_completeness_checker(Structure_manager<? extends Structure> structure_manager) {
		this.structure_manager = structure_manager;
	}

	@Override
	public void run() {
		int remove_num = structure_manager.check_structures_completeness();
		if (remove_num > 0) {
			structure_manager.get_logger().info("自动移除了" + remove_num + "个无效结构");
		}
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
		if (structure_manager instanceof Lottery_pool_manager) {
			return false;
		} else {
			return true;
		}
	}
}
