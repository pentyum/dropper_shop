package com.piggest.minecraft.bukkit.lottery_pool;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Lottery_pool extends Multi_block_structure {

	@Override
	public void on_right_click(Player player) {
		// TODO 自动生成的方法存根

	}

	@Override
	public int completed() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public boolean in_structure(Location loc) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected boolean on_break(Player player) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean create_condition(Player player) {
		// TODO 自动生成的方法存根
		return false;
	}

}
