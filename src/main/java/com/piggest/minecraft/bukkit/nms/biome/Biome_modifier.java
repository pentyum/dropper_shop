package com.piggest.minecraft.bukkit.nms.biome;

import org.bukkit.block.Biome;

public interface Biome_modifier {
	public void set_temperature(Biome biome, float temp);

	public float get_temperature(Biome biome);

	public Biome_storage_modifier get_biome_storge_modifier();
}
