package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Powder {
	public static void init_powder() {
		Powder.register_powder("iron_powder", "铁粉");
		Powder.register_powder("gold_powder", "金粉");
		Powder.register_powder("coal_powder", "煤粉");
		Powder.register_powder("lapis_powder", "青金石粉");
		Powder.register_powder("copper_powder", "铜粉");
		Powder.register_powder("al_powder", "铝粉");
		Powder.register_powder("sn_powder", "锡粉");
		Powder.register_powder("sliver_powder", "银粉");
		Powder.register_powder("cusn_powder", "青铜粉");
		Powder.register_powder("green_powder", "绿宝石粉");
		Powder.register_powder("wheat_powder", "面粉");
	}

	public static void register_powder(String id_name, String name) {
		ItemStack item = new ItemStack(Material.SUGAR);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§r" + name);
		item.setItemMeta(itemmeta);
		Material_ext.register(id_name, item);
	}

	public static boolean is_powder(ItemStack item, String id_name) {
		if (item.getType() != Material.SUGAR) {
			return false;
		}
		if (Material_ext.get_material(id_name) == null) {
			return false;
		} else {
			return id_name.contains("powder");
		}
	}
}
