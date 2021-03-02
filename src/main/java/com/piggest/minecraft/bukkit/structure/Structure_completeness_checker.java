package com.piggest.minecraft.bukkit.structure;

import com.piggest.minecraft.bukkit.lottery_pool.Lottery_pool_manager;

public class Structure_completeness_checker extends Structure_manager_runner {
	public Structure_completeness_checker(Structure_manager<? extends Structure> manager) {
		super(manager);
	}

	@Override
	public int exec() {
		int[] num = manager.check_structures_completeness();
		if (num[1] > 0) {
			manager.get_logger().info("自动移除了" + num[1] + "个无效结构");
		}
		return num[0];
	}

	@Override
	public int get_cycle() {
		return 1000;
	}

	@Override
	public int get_delay() {
		return 1000;
	}

	@Override
	public boolean is_asynchronously() {
		if (manager instanceof Lottery_pool_manager) {
			return false;
		} else {
			return true;
		}
	}
}
