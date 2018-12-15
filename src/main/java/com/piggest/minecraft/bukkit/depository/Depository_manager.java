package com.piggest.minecraft.bukkit.depository;

import org.bukkit.Location;
import org.bukkit.Material;

import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Depository_manager extends Structure_manager {
	public static Depository_manager instance = null;

	public Depository_manager() {
		super(Depository.class);
		Depository_manager.instance = this;
	}

	public Depository find(String player_name, Location loc, boolean new_deop) {
		int x;
		int y;
		int z;
		Depository depository;
		for (x = -1; x <= 1; x++) {
			for (y = -1; y <= 1; y++) {
				for (z = -1; z <= 1; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					// Bukkit.getLogger().info("正在搜索"+check_loc.toString());
					if (material == Material.END_ROD) {
						// Bukkit.getLogger().info("在" + check_loc.toString() + "找到了末地烛");
						if (new_deop == true) {
							depository = new Depository();
							depository.set_location(check_loc);
							if (depository.completed() > 0) {
								depository.set_owner(player_name);
								return depository;
							}
						} else {
							depository = this.get(check_loc);
							if (player_name != null) {
								if (depository != null && depository.get_owner_name().equalsIgnoreCase(player_name)) {
									return depository;
								}
							} else {
								if (depository != null) {
									return depository;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public Depository get(Location location) {
		return (Depository) super.get(location);
	}
}
