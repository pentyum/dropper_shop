package com.piggest.minecraft.bukkit.nms.biome;

import java.lang.reflect.Field;
import org.bukkit.block.Biome;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

import net.minecraft.server.v1_15_R1.BiomeBase;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;

public class Biome_modifier_1_15 implements Biome_modifier {
	//HashMap<Biome, Field> temp_field = new HashMap<Biome, Field>();
	private Field temp_field;
	
	public void init() {
		Class<BiomeBase> biome_nms_class = BiomeBase.class;
		try {
			temp_field = biome_nms_class.getDeclaredField("i");
			temp_field.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			Dropper_shop_plugin.instance.getLogger().warning(biome_nms_class.getName() + "没有i字段!");
			e.printStackTrace();
		}
		/*
		for (Biome biome : Biome.values()) {
			MinecraftKey key = new MinecraftKey(biome.getKey().toString());
			BiomeBase biome_nms = IRegistry.BIOME.get(key);
			Class<? extends BiomeBase> biome_nms_class = biome_nms.getClass();
			try {
				Field field = biome_nms_class.getDeclaredField("i");
				field.setAccessible(true);
				temp_field.put(biome, field);
			} catch (NoSuchFieldException | SecurityException e) {
				Dropper_shop_plugin.instance.getLogger().warning(biome_nms_class.getName() + "没有i字段!");
				e.printStackTrace();
			}
		}
		*/
	}

	public void set_temperature(Biome biome, float temp) {
		MinecraftKey key = new MinecraftKey(biome.getKey().toString());
		BiomeBase biome_nms = IRegistry.BIOME.get(key);
		try {
			temp_field.set(biome_nms, temp);
			//temp_field.get(biome).set(biome_nms, temp);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
