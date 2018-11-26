package com.piggest.minecraft.bukkit.material_ext;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Material_ext {
	private static HashMap<String, ItemStack> ext_material_map = new HashMap<String, ItemStack>();

	public static String get_name(Material material) {
		return material.name();
	}

	public static String get_name(ItemStack itemstack) {
		if (itemstack.hasItemMeta() == true) {
			if (itemstack.getItemMeta().hasDisplayName() == true) {
				for (Entry<String, ItemStack> entry : ext_material_map.entrySet()) {
					if (itemstack.isSimilar(entry.getValue())) {
						return entry.getKey();
					}
				}
			}
		}
		return itemstack.getType().name();
	}

	public static ItemStack new_item(String name, int num) {
		Material material = Material.getMaterial(name);
		if (material != null) {
			return new ItemStack(material, num);
		} else {
			ItemStack new_item = Material_ext.ext_material_map.get(name).clone();
			new_item.setAmount(num);
			return new_item;
		}
	}

	public static ItemStack register(String name, ItemStack itemstack) {
		return Material_ext.ext_material_map.put(name, itemstack.clone());
	}
	
	public static Material get_material(String name) {
		ItemStack item = Material_ext.ext_material_map.get(name);
		if(item!=null) {
			return item.getType();
		}else {
			return Material.getMaterial(name);
		}
	}
}
