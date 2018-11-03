package com.piggest.minecraft.bukkit.depository;

import org.bukkit.Location;

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
}
