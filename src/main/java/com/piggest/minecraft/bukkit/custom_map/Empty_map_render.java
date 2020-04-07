package com.piggest.minecraft.bukkit.custom_map;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

public class Empty_map_render extends Custom_map_render implements ConfigurationSerializable {

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
	}

	@Override
	public @Nonnull Map<String, Object> serialize() {
		return new HashMap<String, Object>();
	}

	public static Empty_map_render deserialize(@Nonnull Map<String, Object> args) {
		return new Empty_map_render();
	}

	@Override
	public void refresh(MapView map, MapCanvas canvas) {
	}
}
