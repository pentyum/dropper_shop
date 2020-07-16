package com.piggest.minecraft.bukkit.tools;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public enum Tool_type {
	PICKAXE("镐"), AXE("斧"), HOE("锄"), SHOVEL("锹"), SWORD("剑");
	private String name;

	Tool_type(String name) {
		this.name = name;
	}

	public String get_display_name() {
		return this.name;
	}

	@Nullable
	public static Tool_type get_tool_type(ItemStack item) {
		String id_name = Material_ext.get_id_name(item);
		String[] splitted = id_name.split("_");
		switch (splitted[1]) {
			case "pickaxe":
				return PICKAXE;
			case "axe":
				return AXE;
			case "hoe":
				return HOE;
			case "shovel":
				return SHOVEL;
			case "sword":
				return SWORD;
			default:
				return null;
		}
	}
}
