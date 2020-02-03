package com.piggest.minecraft.bukkit.grinder;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Ingot extends Material_ext {
	public final static int ingot_model_offset = 100;
	public final static Material model_base = Material.BRICK;
	public final static ArrayList<Ingot> ingot_config = new ArrayList<>();
	public final static HashMap<String, Ingot> ingot_map = new HashMap<>();

	public Ingot(String material_name, String chinese_name) {
		super(material_name, chinese_name);
	}

	public static void init_ingot_config() {
		ingot_config.add(null);
		ingot_config.add(new Ingot("copper", "铜锭"));
		ingot_config.add(new Ingot("aluminium", "铝锭"));
		ingot_config.add(new Ingot("tin", "锡锭"));
		ingot_config.add(new Ingot("silver", "银锭"));
		ingot_config.add(new Ingot("bronze", "青铜锭"));
	}

	public static void init_ingot() {
		init_ingot_config();
		Dropper_shop_plugin.instance.getLogger().info("注册金属锭，原型:" + model_base.getKey().toString());
		for (int i = 1; i < ingot_config.size(); i++) {
			String id_name = ingot_config.get(i).material_name + "_ingot";
			NamespacedKey key = Dropper_shop_plugin.get_key(id_name);
			Material_ext.register(key, model_base, ingot_config.get(i).chinese_name,
					Dropper_shop_plugin.custom_model_data_offset + ingot_model_offset + i);
			ingot_map.put(key.toString(), ingot_config.get(i));
		}
	}

}
