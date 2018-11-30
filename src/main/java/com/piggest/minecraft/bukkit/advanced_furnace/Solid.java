package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.inventory.ItemStack;

public enum Solid implements Chemical {
	iron_powder(1000), iron_ingot(1000),;

	private int unit;

	Solid(int unit) {
		this.unit = unit;
	}
	
	public static Solid get_solid(ItemStack itemstack) {
		return null;
	}
}
