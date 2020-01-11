package com.piggest.minecraft.bukkit.nms.biome;

import java.lang.reflect.Field;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import net.minecraft.server.v1_15_R1.BiomeBase;
import net.minecraft.server.v1_15_R1.BiomeStorage;

public class Biome_storage_modifier_1_15 implements Biome_storage_modifier {
	private Field storage_field;

	public Biome_storage_modifier_1_15() {
		Class<BiomeStorage> biomestorage_nms_class = BiomeStorage.class;
		try {
			storage_field = biomestorage_nms_class.getDeclaredField("g");
			storage_field.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			Dropper_shop_plugin.instance.getLogger().warning(biomestorage_nms_class.getName() + "没有g字段!");
			e.printStackTrace();
		}
	}

	public BiomeBase[] get_biomes(BiomeStorage storage) {
		try {
			return (BiomeBase[]) storage_field.get(storage);
		} catch (IllegalArgumentException | NullPointerException | IllegalAccessException e) {
			return null;
		}
	}

	public void set_biomes(BiomeStorage storage, BiomeBase[] biomes) {
		try {
			storage_field.set(storage, biomes);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
