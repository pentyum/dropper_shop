package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public enum Solid implements Chemical {
	iron_powder(1000), IRON_INGOT(1000),;
	private int unit;

	Solid(int unit) {
		this.unit = unit;
	}

	public static Solid get_solid(ItemStack itemstack) {
		return Solid.valueOf(Material_ext.get_id_name(itemstack));
	}

	public int get_unit() {
		return this.unit;
	}

	public ItemStack get_item_stack() {
		return Material_ext.new_item(this.name(), 1);
	}

	public String get_displayname() {
		return this.name() + "(s)";
	}

	public String get_name() {
		return this.name();
	}
}
