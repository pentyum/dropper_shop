package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.Location;
import org.bukkit.Material;

import com.piggest.minecraft.bukkit.structure.Multi_block_structure;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure_manager;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Advanced_furnace_manager extends Structure_manager<Advanced_furnace>
		implements Multi_block_structure_manager {
	public static Advanced_furnace_manager instance = null;

	public Advanced_furnace_manager() {
		super(Advanced_furnace.class);
		Advanced_furnace_manager.instance = this;
	}

	public Advanced_furnace find(Location loc, boolean new_deop) {
		int x;
		int y;
		int z;
		Advanced_furnace adv_furnace;
		for (x = -1; x <= 1; x++) {
			for (y = -1; y <= 1; y++) {
				for (z = -1; z <= 1; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					// Bukkit.getLogger().info("正在搜索"+check_loc.toString());
					if (material == Material.FURNACE) {
						// Bukkit.getLogger().info("在" + check_loc.toString() + "找到了末地烛");
						if (new_deop == true) {
							adv_furnace = new Advanced_furnace();
							adv_furnace.set_location(check_loc);
							adv_furnace.set_temperature(adv_furnace.get_base_temperature());
							if (adv_furnace.completed() > 0) {
								return adv_furnace;
							}
						} else {
							adv_furnace = this.get(check_loc);
							if (adv_furnace != null) {
								return adv_furnace;
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public Advanced_furnace find(String player_name, Location loc, boolean new_structure) {
		return this.find(loc, new_structure);
	}

	@Override
	public void add(Multi_block_structure structure) {
		super.add((Advanced_furnace) structure);
	}
}
