package com.piggest.minecraft.bukkit.utils.language;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Item_zh_cn {
	public static final HashMap<String, String> name = new HashMap<String, String>();

	public static void init() {
		name.put("minecraft:stone", "石头");
	}

	public static String get_enchantment_name(ItemStack item) {
		String full_name = Material_ext.get_full_name(item);
		String result = name.get(full_name);
		if (result == null) {
			return full_name;
		} else {
			return result;
		}
	}
}
