package com.piggest.minecraft.bukkit.trees_felling_machine;

import org.bukkit.Location;
import org.bukkit.Material;

import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Trees_felling_machine_manager extends Structure_manager {

	public Trees_felling_machine_manager() {
		super(Trees_felling_machine.class);
	}

	@Override
	public Trees_felling_machine find(String player_name, Location loc, boolean new_structure) {
		return this.find(loc, new_structure);
	}

	public Trees_felling_machine find(Location loc, boolean new_structure) {
		int x;
		int y;
		int z;
		Trees_felling_machine machine;
		for (x = -1; x <= 1; x++) {
			for (y = -1; y <= 1; y++) {
				for (z = -1; z <= 1; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					if (material == Material.STONECUTTER) {
						if (new_structure == true) {
							machine = new Trees_felling_machine();
							machine.set_location(check_loc);
							if (machine.completed() > 0) {
								return machine;
							}
						} else {
							machine = this.get(check_loc);
							if (machine != null) {
								return machine;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public Trees_felling_machine get(Location loc) {
		return (Trees_felling_machine) super.get(loc);
	}
}
