package com.piggest.minecraft.bukkit.material_ext;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Material_ext {
	private static HashMap<String, ItemStack> ext_material_map = new HashMap<String, ItemStack>();

	public static String get_name(Material material) {
		return material.name();
	}

	public static String get_display_name(ItemStack itemstack) {
		if (itemstack.hasItemMeta() == true) {
			ItemMeta meta = itemstack.getItemMeta();
			if (meta.hasDisplayName() == true) {
				return meta.getDisplayName().substring(2, meta.getDisplayName().length());
			}
		}
		return itemstack.getType().name();
	}

	public static String get_id_name(ItemStack itemstack) {
		if (itemstack.hasItemMeta() == true) {
			ItemMeta meta = itemstack.getItemMeta();
			if (meta.hasDisplayName() == true) {
				for (Entry<String, ItemStack> entry : ext_material_map.entrySet()) {
					if (itemstack.isSimilar(entry.getValue())) {
						return entry.getKey();
					}
				}
			}
		}
		return itemstack.getType().name();
	}

	public static ItemStack new_item(String id_name, int num) {
		Material material = Material.getMaterial(id_name);
		if (material != null) {
			return new ItemStack(material, num);
		} else {
			ItemStack new_item = Material_ext.ext_material_map.get(id_name).clone();
			new_item.setAmount(num);
			return new_item;
		}
	}

	public static ItemStack register(String id_name, ItemStack itemstack) {

		return Material_ext.ext_material_map.put(id_name, itemstack.clone());
	}

	public static Material get_material(String id_name) {
		ItemStack item = Material_ext.ext_material_map.get(id_name);
		if (item != null) {
			return item.getType();
		} else {
			return Material.getMaterial(id_name);
		}
	}
}
