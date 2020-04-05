package com.piggest.minecraft.bukkit.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.custom_map.Analog_clock_map_render;
import com.piggest.minecraft.bukkit.custom_map.Character_map_render;
import com.piggest.minecraft.bukkit.custom_map.Character_section_map_render;
import com.piggest.minecraft.bukkit.custom_map.Custom_map_render;
import com.piggest.minecraft.bukkit.custom_map.Digital_clock_map_render;

public class Map_config extends Ext_config {
	private HashMap<Integer, MapView> custom_map_map = new HashMap<>();

	public Map_config() {
		super("maps.yml");
		ConfigurationSerialization.registerClass(Character_map_render.class);
		ConfigurationSerialization.registerClass(Character_section_map_render.class);
		ConfigurationSerialization.registerClass(Digital_clock_map_render.class);
		ConfigurationSerialization.registerClass(Analog_clock_map_render.class);
	}

	public MapView create_new_map(Player player, Custom_map_render new_render) {
		MapView mapview = Bukkit.getServer().createMap(player.getWorld());
		replace_render(mapview, new_render);
		this.set("map_" + (mapview.getId()), new_render);
		return mapview;
	}

	public void reload() {
		this.load();
		for (Entry<Integer, MapView> entry : this.custom_map_map.entrySet()) {
			int id = entry.getKey();
			MapView map = entry.getValue();
			Custom_map_render new_render = (Custom_map_render) this.get_config().get("map_" + id);
			replace_render(map, new_render);
		}
	}

	public void replace_render(MapView map, MapRenderer new_render) {
		List<MapRenderer> renders = map.getRenderers();
		for (MapRenderer render : renders) {
			map.removeRenderer(render);
		}
		map.addRenderer(new_render);
		if (new_render instanceof Custom_map_render) {
			this.custom_map_map.put(map.getId(), map);
		}
	}

}
