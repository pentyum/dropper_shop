package com.piggest.minecraft.bukkit.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;

public class World_structure_config extends Ext_config {
	public World_structure_config(World world) {
		super("shops_" + world.getName() + ".yml");
	}

	public List<Map<?, ?>> getMapList(String structure_name) {
		return this.config.getMapList(structure_name);
	}

	public void set(String structure_name, ArrayList<HashMap<String, Object>> value) {
		this.config.set(structure_name, value);
	}
}
