package com.piggest.minecraft.bukkit.material_ext;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Material_ext {
	private static HashMap<String, ItemStack> ext_material_map = new HashMap<String, ItemStack>();

	public static String get_name(Material material) { // 获得材质名称
		return material.name();
	}

	public static String get_display_name(ItemStack itemstack) { // 获得显示名称
		if (itemstack.hasItemMeta() == true) {
			ItemMeta meta = itemstack.getItemMeta();
			if (meta.hasDisplayName() == true) {
				return meta.getDisplayName().substring(2, meta.getDisplayName().length());
			}
		}
		return itemstack.getType().name();
	}

	public static String get_id_name(ItemStack itemstack) { // 获得内部ID名称
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

	public static ItemStack new_item(String id_name, int num) { // 根据内部ID生成ItemStack，等效于new ItemStack(Material)
		return new_item(id_name, num, null);
	}

	public static ItemStack new_item(String id_name, int num, Map<Enchantment, Integer> enchantments) {
		Material material = Material.getMaterial(id_name);
		ItemStack new_item = null;
		if (material != null) {
			new_item = new ItemStack(material, num);
		} else {
			new_item = Material_ext.ext_material_map.get(id_name).clone();
			new_item.setAmount(num);
		}
		if (enchantments != null) {
			new_item.addEnchantments(enchantments);
		}
		return new_item;
	}

	public static ItemStack register(String id_name, ItemStack itemstack) {

		return Material_ext.ext_material_map.put(id_name, itemstack.clone());
	}

	public static Material get_material(String id_name) { // 根据内部ID获得材质
		ItemStack item = Material_ext.ext_material_map.get(id_name);
		if (item != null) {
			return item.getType();
		} else {
			return Material.getMaterial(id_name);
		}
	}
}
