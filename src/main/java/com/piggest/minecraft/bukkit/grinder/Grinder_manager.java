package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.Location;

import com.piggest.minecraft.bukkit.structure.Multi_block_structure;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure_manager;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Grinder_manager extends Structure_manager<Grinder> implements Multi_block_structure_manager {

	public Grinder_manager() {
		super(Grinder.class);
	}

	@Override
	public Multi_block_structure find(String player_name, Location loc, boolean new_structure) {
		return this.get(loc);
	}

	@Override
	public void add(Multi_block_structure structure) {
		super.add((Grinder) structure);
	}

}