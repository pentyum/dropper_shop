package com.piggest.minecraft.bukkit.exp_saver;

import org.bukkit.Location;
import org.bukkit.Material;

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
		int x;
		int y;
		int z;
		Exp_saver exp_saver;
		for (x = -1; x <= 1; x++) {
			for (y = 0; y <= 2; y++) {
				for (z = -1; z <= 1; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					if (material == Material.DIAMOND_BLOCK) {
						if (new_structure == true) {
							exp_saver = new Exp_saver();
							exp_saver.set_location(check_loc);
							if (exp_saver.completed() > 0) {
								return exp_saver;
							}
						} else {
							exp_saver = (Exp_saver) this.get(check_loc);
							if (exp_saver != null) {
								return exp_saver;
							}
						}
					}
				}
			}
		}
		return null;
	}

}
