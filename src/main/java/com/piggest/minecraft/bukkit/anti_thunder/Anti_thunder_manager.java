package com.piggest.minecraft.bukkit.anti_thunder;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Anti_thunder_manager extends Structure_manager<Anti_thunder> {
	public static Anti_thunder_manager instance;
	private Material[][][] model = {
			{ { Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK },
					{ Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK },
					{ Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK } },
			{ { null, null, null }, { null, Material.PISTON, null }, { null, null, null } } };
	private int center_x = 1;
	private int center_y = 1;
	private int center_z = 1;

	public Anti_thunder_manager() {
		super(Anti_thunder.class);
		instance = this;
	}
	/*
	@Override
	public Anti_thunder find(String player_name, Location loc, boolean new_structure) {
		int x;
		int y;
		int z;
		Anti_thunder anti_thunder;
		for (x = -1; x <= 1; x++) {
			for (y = 0; y <= 1; y++) {
				for (z = -1; z <= 1; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					// Bukkit.getLogger().info("正在搜索"+check_loc.toString());
					if (material == Material.PISTON) {
						// Bukkit.getLogger().info("在" + check_loc.toString() + "找到了末地烛");
						if (new_structure == true) {
							anti_thunder = new Anti_thunder();
							anti_thunder.set_location(check_loc);
							anti_thunder.set_owner(player_name);
							if (anti_thunder.completed() == true) {
								return anti_thunder;
							}
						} else {
							anti_thunder = this.get(check_loc);
							if (anti_thunder != null) {
								return anti_thunder;
							}
						}
					}
				}
			}
		}
		return null;
	}
	*/
	
	@Override
	public String get_permission_head() {
		return "anti_thunder";
	}

	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		return new int[] { this.center_x, this.center_y, this.center_z };
	}

	public int get_cycle() {
		return Dropper_shop_plugin.instance.get_config().getInt("anti-thunder-cycle");
	}

	public int get_price() {
		return Dropper_shop_plugin.instance.get_price_config().get_anti_thunder_price();
	}

}
