package com.piggest.minecraft.bukkit.lottery_pool;

import org.bukkit.Location;
import org.bukkit.Material;

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
		if (new_structure == false) {
			return this.get(loc);
		} else {
			Lottery_pool lottery_pool = new Lottery_pool();
			lottery_pool.set_location(loc);
			if (lottery_pool.completed() == false) {
				return null;
			} else {
				return lottery_pool;
			}
		}
	}

	@Override
	public String get_permission_head() {
		return "lottery";
	}
	
	@Override
	public Material[][][] get_model() {
		return null;
	}

	@Override
	public int[] get_center() {
		return null;
	}
}
