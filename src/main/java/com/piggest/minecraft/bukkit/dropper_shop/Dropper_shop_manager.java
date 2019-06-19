package com.piggest.minecraft.bukkit.dropper_shop;

import org.bukkit.Location;
import org.bukkit.Material;

import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Dropper_shop_manager extends Structure_manager {
	public static Dropper_shop_manager instance = null;

	public Dropper_shop_manager() {
		super(Dropper_shop.class);
		Dropper_shop_manager.instance = this;
	}

	@Override
	public Dropper_shop find(String player_name, Location loc, boolean new_structure) {
		if (new_structure == false) {
			Dropper_shop shop = this.get(loc);
			if (shop == null) {
				return null;
			}
			if (player_name != null) {
				if (!shop.get_owner_name().equalsIgnoreCase(player_name)) {
					return null;
				}
			}
			return shop;
		} else {
			if (loc.getBlock().getType() == Material.DROPPER) {
				Dropper_shop shop = new Dropper_shop();
				shop.set_location(loc);
				shop.set_owner(player_name);
				return shop;
			} else {
				return null;
			}
		}
	}

	@Override
	public Dropper_shop get(Location loc) {
		return (Dropper_shop) super.get(loc);
	}

	@Override
	public String get_permission_head() {
		return "dropper_shop";
	}
}
