package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public enum Liquid implements Chemical {
	water("水"),lava("熔岩");

	private String display_name;

	Liquid(String display_name) {
		this.display_name = display_name;
	}

	@Override
	public String get_displayname() {
		return this.display_name + "(l)";
	}

	@Override
	public String get_name() {
		return this.name();
	}
	public static int get_item_unit(ItemStack item) {
		String id_name = Material_ext.get_id_name(item);
		switch (id_name) {
		case "water_bucket":
			return 1000;
		case "lava_bucket":
			return 1000;
		default:
			return 0;
		}
	}
	public static Liquid get_liquid(ItemStack item) {
		String id_name = Material_ext.get_id_name(item);
		switch (id_name) {
		case "water_bucket":
			return water;
		case "lava_bucket":
			return lava;
		default:
			return null;
		}
	}
	public static Liquid get_liquid(String name) {
		Liquid liquid = null;
		try {
			liquid = Liquid.valueOf(name);
		} catch (Exception e) {
		} finally {
		}
		return liquid;
	}
	
	public ItemStack get_filled_bucket() {
		return Material_ext.new_item(this.name()+"_bucket", 1);
	}
}
