package com.piggest.minecraft.bukkit.nms.biome;

import org.bukkit.block.Biome;

public interface Biome_modifier {
	public void init();
	public void set_temperature(Biome biome, float temp);
}
