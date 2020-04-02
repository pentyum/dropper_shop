package com.piggest.minecraft.bukkit.config;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

import com.piggest.minecraft.bukkit.custom_map.Character_map_render;

public class Map_config extends Ext_config {
	public Map_config() {
		super("maps.yml");
		ConfigurationSerialization.registerClass(Character_map_render.class);
	}

}
