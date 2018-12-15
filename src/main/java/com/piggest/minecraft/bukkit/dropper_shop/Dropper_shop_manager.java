package com.piggest.minecraft.bukkit.dropper_shop;

import org.bukkit.Location;

import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Dropper_shop_manager extends Structure_manager {
	public static Dropper_shop_manager instance = null;
	// private HashMap<Location, Dropper_shop> shops_map = new HashMap<Location,
	// Dropper_shop>();

	public Dropper_shop_manager() {
		super(Dropper_shop.class);
		Dropper_shop_manager.instance = this;
	}

	@Override
	public Dropper_shop find(String player_name, Location loc, boolean new_structure) {
		Dropper_shop shop = this.get(loc);
		if (player_name != null) {
			if (!shop.get_owner_name().equalsIgnoreCase(player_name)) {
				return null;
			}
		}
		return shop;
	}

	@Override
	public Dropper_shop get(Location loc) {
		return (Dropper_shop) super.get(loc);
	}
}
