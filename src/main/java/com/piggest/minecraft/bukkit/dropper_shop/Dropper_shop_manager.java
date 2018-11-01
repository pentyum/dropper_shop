package com.piggest.minecraft.bukkit.dropper_shop;

import java.util.HashMap;

import org.bukkit.Location;

public class Dropper_shop_manager {
	private Dropper_shop_plugin plugin = null;
	private HashMap<Location, Dropper_shop> shops_map = new HashMap<Location, Dropper_shop>();

	public Dropper_shop_manager(Dropper_shop_plugin plugin) {
		this.plugin = plugin;
	}

	public void load_shops() {
		// TODO Auto-generated method stub

	}

	public void save_shops() {
		// TODO Auto-generated method stub

	}

	public Dropper_shop get_dropper_shop(Location loc) {
		return this.shops_map.get(loc);
	}
	
}
