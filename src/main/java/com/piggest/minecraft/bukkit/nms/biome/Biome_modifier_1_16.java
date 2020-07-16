package com.piggest.minecraft.bukkit.nms.biome;

import com.piggest.minecraft.bukkit.biome_modify.Biome_modify;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import net.minecraft.server.v1_16_R1.BiomeBase;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_16_R1.block.CraftBlock;

import java.lang.reflect.Field;

public class Biome_modifier_1_16 implements Biome_modifier {
	private Field temp_field;
	//private HashMap<Biome, BiomeBase> biome_map = new HashMap<>();
	//private HashMap<BiomeBase, Biome> biome_map_reverse = new HashMap<>();
	private Biome_storage_modifier_1_16 biome_storge_modifier = null;

	public Biome_modifier_1_16() {
		Class<BiomeBase> biome_nms_class = BiomeBase.class;
		try {
			temp_field = biome_nms_class.getDeclaredField("i");
			temp_field.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			Dropper_shop_plugin.instance.getLogger().warning(biome_nms_class.getName() + "没有i字段!");
			e.printStackTrace();
		}

		Dropper_shop_plugin.instance.getLogger().info("开始保存生物群系原温度");
		for (Biome biome : Biome.values()) {
			/*
			MinecraftKey key = new MinecraftKey(biome.getKey().toString());
			BiomeBase biome_nms = IRegistry.BIOME.get(key);
			biome_map.put(biome, biome_nms);
			biome_map_reverse.put(biome_nms, biome);
			*/
			BiomeBase biome_nms = get_biomebase(biome);
			Biome_modify.original_temp.put(biome, biome_nms.getTemperature());
		}
		this.biome_storge_modifier = new Biome_storage_modifier_1_16();
	}

	public void set_temperature(Biome biome, float temp) {
		BiomeBase biome_nms = get_biomebase(biome);
		try {
			temp_field.set(biome_nms, temp);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public float get_temperature(Biome biome) {
		BiomeBase biome_nms = get_biomebase(biome);
		return biome_nms.getTemperature();
	}

	@Override
	public Biome_storage_modifier get_biome_storge_modifier() {
		return this.biome_storge_modifier;
	}

	public BiomeBase get_biomebase(Biome biome) {
		return CraftBlock.biomeToBiomeBase(biome);
	}

	public Biome get_biome(BiomeBase biomeBase) {
		return CraftBlock.biomeBaseToBiome(biomeBase);
	}
}
