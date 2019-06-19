package com.piggest.minecraft.bukkit.electrolyte;

import org.bukkit.Location;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Electrolyte_manager extends Structure_manager {

	public static Electrolyte_manager instance = null;

	public Electrolyte_manager() {
		super(Electrolyte.class);
		Electrolyte_manager.instance = this;
	}

	@Override
	public Structure find(String player_name, Location loc, boolean new_structure) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String get_permission_head() {
		// TODO 自动生成的方法存根
		return null;
	}

}
