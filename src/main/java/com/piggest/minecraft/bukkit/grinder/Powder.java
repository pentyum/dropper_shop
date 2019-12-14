package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Powder {
	public static void init_powder() {
		Powder.register_powder("iron_powder", "铁粉", 1);
		Powder.register_powder("gold_powder", "金粉", 2);
		Powder.register_powder("coal_powder", "煤粉", 3);
		Powder.register_powder("lapis_powder", "青金石粉", 4);
		Powder.register_powder("copper_powder", "铜粉", 5);
		Powder.register_powder("aluminium_powder", "铝粉", 6);
		Powder.register_powder("tin_powder", "锡粉", 7);
		Powder.register_powder("silver_powder", "银粉", 8);
		Powder.register_powder("bronze_powder", "青铜粉", 9);
		Powder.register_powder("emerald_powder", "绿宝石粉", 10);
		Powder.register_powder("wheat_powder", "面粉", 11);
	}

	public static void register_powder(String id_name, String name, int powder_id) {
		ItemStack item = new ItemStack(Material.SUGAR);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§r" + name);
		itemmeta.setCustomModelData(Dropper_shop_plugin.custom_model_data_offset + 100 + powder_id);
		item.setItemMeta(itemmeta);
		Material_ext.register(id_name, item);
	}

	public static boolean is_powder(ItemStack item, String id_name) {
		if (item.getType() != Material.SUGAR) {
			return false;
		}
		return Material_ext.get_id_name(item).equalsIgnoreCase(id_name);
	}
}
