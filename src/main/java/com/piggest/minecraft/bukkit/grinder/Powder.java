package com.piggest.minecraft.bukkit.grinder;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class Powder extends Material_ext {
	public final static int powder_model_offset = 100;
	public final static Material model_base = Material.SUGAR;

	private NamespacedKey ingot = null;

	public NamespacedKey get_ingot_namespacedkey() {
		return this.ingot;
	}

	public Powder(String material_name, String chinese_name) {
		super(material_name, chinese_name);
	}

	public Powder(String material_name, String chinese_name, NamespacedKey ingot) {
		super(material_name, chinese_name);
		this.ingot = ingot;
	}

	public final static ArrayList<Powder> powder_config = new ArrayList<>();
	public final static HashMap<String, Powder> powder_map = new HashMap<>();

	public static void init_powder_config() {
		powder_config.add(null);
		powder_config.add(new Powder("iron", "铁粉", Material.IRON_INGOT.getKey()));
		powder_config.add(new Powder("gold", "金粉", Material.GOLD_INGOT.getKey()));
		powder_config.add(new Powder("coal", "煤粉", Material.COAL.getKey()));
		powder_config.add(new Powder("lapis", "青金石粉", Material.LAPIS_LAZULI.getKey()));
		powder_config.add(new Powder("copper", "铜粉", Dropper_shop_plugin.get_key("copper_ingot")));
		powder_config.add(new Powder("aluminium", "铝粉", Dropper_shop_plugin.get_key("aluminium_ingot")));
		powder_config.add(new Powder("tin", "锡粉", Dropper_shop_plugin.get_key("tin_ingot")));
		powder_config.add(new Powder("silver", "银粉", Dropper_shop_plugin.get_key("silver_ingot")));
		powder_config.add(new Powder("bronze", "青铜粉", Dropper_shop_plugin.get_key("bronze_ingot")));
		powder_config.add(new Powder("emerald", "绿宝石粉", Material.EMERALD.getKey()));
		powder_config.add(new Powder("flour", "面粉"));
	}

	public static void init_powder() {
		init_powder_config();
		Dropper_shop_plugin.instance.getLogger().info("注册粉末，原型:" + model_base.getKey().toString());
		for (int i = 1; i < powder_config.size(); i++) {
			String id_name = powder_config.get(i).material_name + "_powder";
			NamespacedKey key = Dropper_shop_plugin.get_key(id_name);
			Material_ext.register(key, model_base, powder_config.get(i).chinese_name,
					Dropper_shop_plugin.custom_model_data_offset + powder_model_offset + i);
			powder_map.put(key.toString(), powder_config.get(i));
		}
	}

	public static boolean is_powder(ItemStack item, String id_name) {
		if (item.getType() != Powder.model_base) {
			return false;
		}
		return Material_ext.get_id_name(item).equalsIgnoreCase(id_name);
	}
}
