package com.piggest.minecraft.bukkit.lottery_pool;

import org.bukkit.Location;

import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Lottery_pool_manager extends Structure_manager {
	
	public static Lottery_pool_manager instance = null;

	public Lottery_pool_manager() {
		super(Lottery_pool.class);
		Lottery_pool_manager.instance = this;
	}
	
	@Override
	public Structure find(String player_name, Location loc, boolean new_structure) {
		// TODO 自动生成的方法存根
		return null;
	}

}
