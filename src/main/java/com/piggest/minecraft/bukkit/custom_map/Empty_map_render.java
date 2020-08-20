package com.piggest.minecraft.bukkit.custom_map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class Empty_map_render extends Custom_map_render implements ConfigurationSerializable {

	@Override
	public void render(@Nonnull MapView map, @Nonnull MapCanvas canvas, @Nonnull Player player) {
	}

	@Override
	public @Nonnull
	Map<String, Object> serialize() {
		return new HashMap<String, Object>();
	}

	public static Empty_map_render deserialize(@Nonnull Map<String, Object> args) {
		return new Empty_map_render();
	}


}
