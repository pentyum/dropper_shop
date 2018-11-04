package com.piggest.minecraft.bukkit.depository;

import org.bukkit.Location;
import org.bukkit.Material;

import com.piggest.minecraft.bukkit.Structure.Structure_manager;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Depository_manager extends Structure_manager<Depository> {
	public static Depository_manager instance = null;

	public Depository_manager(Dropper_shop_plugin dropper_shop_plugin) {
		super(dropper_shop_plugin, Depository.class);
		Depository_manager.instance = this;
	}

	@Override
	public Depository get(Location loc) {
		return null;
	}

	public Depository find(String player_name, Location loc) {
		int x;
		int y;
		int z;
		Depository depository;
		for (x = -1; x <= 1; x++) {
			for (y = -1; y <= 1; y++) {
				for (z = -1; z <= 1; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					if (material == Material.END_ROD) {
						depository = new Depository();
						depository.set_location(check_loc);
						if (depository.completed() > 0) {
							depository.set_owner(player_name);
							return depository;
						}
					}
				}
			}
		}
		return null;
	}
}
