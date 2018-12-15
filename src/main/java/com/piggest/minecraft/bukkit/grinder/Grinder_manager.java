package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.Location;

import com.piggest.minecraft.bukkit.structure.Multi_block_structure;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Grinder_manager extends Structure_manager {

	public Grinder_manager() {
		super(Grinder.class);
	}

	@Override
	public Multi_block_structure find(String player_name, Location loc, boolean new_structure) {
		if (new_structure == false) {
			return this.get(loc);
		} else {
			Grinder grinder = new Grinder();
			grinder.set_location(loc);
			if (grinder.completed() == 0) {
				return null;
			} else {
				return grinder;
			}
		}
	}

	public Grinder get(Location location) {
		return (Grinder) super.get(location);
	}
}