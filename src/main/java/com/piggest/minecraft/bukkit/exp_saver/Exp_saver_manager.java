package com.piggest.minecraft.bukkit.exp_saver;

import org.bukkit.Location;

import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Exp_saver_manager extends Structure_manager {
	public static Exp_saver_manager instance = null;

	public Exp_saver_manager() {
		super(Exp_saver.class);
		Exp_saver_manager.instance = this;
	}

	@Override
	public Structure find(String player_name, Location loc, boolean new_structure) {
		// TODO Auto-generated method stub
		return null;
	}

}
