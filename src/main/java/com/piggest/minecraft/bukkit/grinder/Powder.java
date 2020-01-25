package com.piggest.minecraft.bukkit.grinder;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Powder {
	public final static int powder_model_offset = 100;
	private String material_name;
	private String chinese_name;
	private String ingot_name = null;

	public String get_material_name() {
		return this.material_name;
	}

	public String get_chinese_name() {
		return this.chinese_name;
	}

	public String get_ingot_name() {
		return this.ingot_name;
	}

	public Powder(String material_name, String chinese_name) {
		this.material_name = material_name;
		this.chinese_name = chinese_name;
	}

	public Powder(String material_name, String chinese_name, String ingot_name) {
		this.material_name = material_name;
		this.chinese_name = chinese_name;
		this.ingot_name = ingot_name;
	}

	public final static ArrayList<Powder> powder_config = new ArrayList<>();
	public final static HashMap<String, Powder> powder_map = new HashMap<>();

	public static void init_powder_config() {
		powder_config.add(null);
		powder_config.add(new Powder("iron", "铁粉", "iron_ingot"));
		powder_config.add(new Powder("gold", "金粉", "gold_ingot"));
		powder_config.add(new Powder("coal", "煤粉", "coal"));
		powder_config.add(new Powder("lapis", "青金石粉", "lapis_lazuli"));
		powder_config.add(new Powder("copper", "铜粉", "copper_ingot"));
		powder_config.add(new Powder("aluminium", "铝粉", "aluminium_ingot"));
		powder_config.add(new Powder("tin", "锡粉", "tin_ingot"));
		powder_config.add(new Powder("silver", "银粉", "silver_ingot"));
		powder_config.add(new Powder("bronze", "青铜粉", "bronze_ingot"));
		powder_config.add(new Powder("emerald", "绿宝石粉", "emerald"));
		powder_config.add(new Powder("flour", "面粉"));
	}

	public static void init_powder() {
		init_powder_config();
		for (int i = 1; i < powder_config.size(); i++) {
			String id_name = powder_config.get(i).material_name + "_powder";
			NamespacedKey key = Dropper_shop_plugin.instance.get_key(id_name);
			Powder.register_powder(key, powder_config.get(i).chinese_name, i);
			powder_map.put(key.toString(), powder_config.get(i));
		}
	}

	public static void register_powder(NamespacedKey full_name, String name, int powder_id) {
		ItemStack item = new ItemStack(Material.SUGAR);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§r" + name);
		itemmeta.setCustomModelData(Dropper_shop_plugin.custom_model_data_offset + powder_model_offset + powder_id);
		item.setItemMeta(itemmeta);
		Material_ext.register(full_name, item);
	}

	public static void init_powder_furnace_recipe() {

	}

	public static boolean is_powder(ItemStack item, String id_name) {
		if (item.getType() != Material.SUGAR) {
			return false;
		}
		return Material_ext.get_id_name(item).equalsIgnoreCase(id_name);
	}
}
