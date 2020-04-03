package com.piggest.minecraft.bukkit.custom_map;

import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public abstract class Custom_map_render extends MapRenderer {
	
	@Override
	public void initialize(MapView map) {
		super.initialize(map);
		map.setLocked(true);
	}

}
