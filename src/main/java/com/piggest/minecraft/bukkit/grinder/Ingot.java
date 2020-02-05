package com.piggest.minecraft.bukkit.grinder;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.material_ext.Tool_material;

public class Ingot extends Material_ext {
	public final static int ingot_model_offset = 100;
	public final static Material model_base = Material.BRICK;
	public final static ArrayList<Ingot> ingot_config = new ArrayList<>();
	public final static HashMap<String, Ingot> ingot_map = new HashMap<>();
	private Tool_material.Custom_material tool_material;

	public Ingot(Tool_material.Custom_material tool_material) {
		super(tool_material.name().toLowerCase(), tool_material.get_display_name() + "锭");
		this.tool_material = tool_material;
	}

	public Tool_material.Custom_material get_tool_material() {
		return this.tool_material;
	}

	public static void init_ingot_config() {
		ingot_config.add(null);
		ingot_config.add(new Ingot(Tool_material.Custom_material.COPPER));
		ingot_config.add(new Ingot(Tool_material.Custom_material.ALUMINUM));
		ingot_config.add(new Ingot(Tool_material.Custom_material.TIN));
		ingot_config.add(new Ingot(Tool_material.Custom_material.SILVER));
		ingot_config.add(new Ingot(Tool_material.Custom_material.BRONZE));
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
