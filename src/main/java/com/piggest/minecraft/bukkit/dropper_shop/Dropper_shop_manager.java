package com.piggest.minecraft.bukkit.dropper_shop;

import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Dropper_shop_manager extends Structure_manager<Dropper_shop> {
	public static Dropper_shop_manager instance = null;
	// private HashMap<Location, Dropper_shop> shops_map = new HashMap<Location,
	// Dropper_shop>();

	public Dropper_shop_manager(Dropper_shop_plugin plugin) {
		super(plugin, Dropper_shop.class);
		Dropper_shop_manager.instance = this;
	}

}
