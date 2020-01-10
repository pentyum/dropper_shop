package com.piggest.minecraft.bukkit.nms.biome;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.block.Biome;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

import net.minecraft.server.v1_15_R1.BiomeBase;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;

public class Biome_modifier_1_15 implements Biome_modifier {
	private Field temp_field;
	private HashMap<Biome, BiomeBase> biome_map = new HashMap<>();

	public void init() {
		Class<BiomeBase> biome_nms_class = BiomeBase.class;
		try {
			temp_field = biome_nms_class.getDeclaredField("i");
			temp_field.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			Dropper_shop_plugin.instance.getLogger().warning(biome_nms_class.getName() + "没有i字段!");
			e.printStackTrace();
		}
		Dropper_shop_plugin.instance.getLogger().info("开始添加生物群系NMS映射");
		for (Biome biome : Biome.values()) {
			MinecraftKey key = new MinecraftKey(biome.getKey().toString());
			BiomeBase biome_nms = IRegistry.BIOME.get(key);
			biome_map.put(biome, biome_nms);
		}
	}

	public void set_temperature(Biome biome, float temp) {
		BiomeBase biome_nms = biome_map.get(biome);
		try {
			temp_field.set(biome_nms, temp);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public float get_temperature(Biome biome) {
		BiomeBase biome_nms = biome_map.get(biome);
		return biome_nms.getTemperature();
	}
}
