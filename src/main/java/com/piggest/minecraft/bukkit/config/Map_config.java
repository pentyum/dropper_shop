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

	public MapView create_new_map(Player player, Custom_map_render content_render, Custom_map_render edge_render) {
		MapView mapview = Bukkit.getServer().createMap(player.getWorld());
		replace_render(mapview, content_render, edge_render);
		this.set_content(mapview.getId(), content_render);
		this.set_edge(mapview.getId(), edge_render);
		return mapview;
	}

	public void set_content(int id, Custom_map_render content_render) {
		this.set("map_" + id + ".content", content_render);
	}

	public void set_edge(int id, Custom_map_render edge_render) {
		this.set("map_" + id + ".edge", edge_render);
	}

	public Custom_map_render get_content(int id) {
		return (Custom_map_render) this.config.get("map_" + id + ".content");
	}

	public Custom_map_render get_edge(int id) {
		return (Custom_map_render) this.config.get("map_" + id + ".edge");
	}

	public void reload() {
		this.load();
		for (Entry<Integer, MapView> entry : this.custom_map_map.entrySet()) {
			int id = entry.getKey();
			MapView map = entry.getValue();
			Custom_map_render edge_render = this.get_edge(id);
			Custom_map_render content_render = this.get_content(id);
			if (content_render != null) {
				replace_render(map, content_render, edge_render);
			}
		}
	}

	public void replace_render(MapView map, MapRenderer content_render, MapRenderer edge_render) {
		List<MapRenderer> renders = map.getRenderers();
		for (MapRenderer render : renders) {
			map.removeRenderer(render);
		}
		if (content_render != null) {
			map.addRenderer(content_render);
		}
		if (edge_render != null) {
			map.addRenderer(edge_render);
		}
		if (content_render instanceof Custom_map_render) {
			this.custom_map_map.put(map.getId(), map);
		}
	}

}
