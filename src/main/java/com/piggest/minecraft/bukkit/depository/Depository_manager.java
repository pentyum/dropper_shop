package com.piggest.minecraft.bukkit.depository;

import org.bukkit.Location;
import org.bukkit.Material;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Depository_manager extends Structure_manager<Depository> {
	public static Depository_manager instance = null;

	public Depository_manager() {
		super(Depository.class);
		Depository_manager.instance = this;
	}

	@Override
	public void add(Depository depository) {
		depository.get_runner().runTaskTimerAsynchronously(Dropper_shop_plugin.instance, 0, 20 * 3600);
		depository.get_importer().runTaskTimer(Dropper_shop_plugin.instance, 10, 10);
		super.add(depository);
	}

	@Override
	public void remove(Depository depository) {
		depository.get_runner().cancel();
		depository.get_importer().cancel();
		super.remove(depository);
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
}
