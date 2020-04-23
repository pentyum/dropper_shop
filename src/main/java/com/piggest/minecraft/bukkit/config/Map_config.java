package com.piggest.minecraft.bukkit.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.custom_map.Background_map_render;
import com.piggest.minecraft.bukkit.custom_map.Character_map_render;
import com.piggest.minecraft.bukkit.custom_map.Character_section_map_render;
import com.piggest.minecraft.bukkit.custom_map.Custom_map_render;
import com.piggest.minecraft.bukkit.custom_map.Empty_map_render;
import com.piggest.minecraft.bukkit.custom_map.Gif_map_render;
import com.piggest.minecraft.bukkit.custom_map.Local_image_map_render;
import com.piggest.minecraft.bukkit.custom_map.Qr_code_map_render;
import com.piggest.minecraft.bukkit.custom_map.clock.Analog_clock_background_map_render;
import com.piggest.minecraft.bukkit.custom_map.clock.Analog_clock_map_render;
import com.piggest.minecraft.bukkit.custom_map.clock.Digital_clock_map_render;

public class Map_config extends Ext_config {
	private HashMap<Integer, MapView> custom_map_map = new HashMap<>();

	public Map_config() {
		super("maps.yml");
		ConfigurationSerialization.registerClass(Empty_map_render.class);
		ConfigurationSerialization.registerClass(Character_map_render.class);
		ConfigurationSerialization.registerClass(Character_section_map_render.class);
		ConfigurationSerialization.registerClass(Digital_clock_map_render.class);
		ConfigurationSerialization.registerClass(Analog_clock_map_render.class);
		ConfigurationSerialization.registerClass(Local_image_map_render.class);
		ConfigurationSerialization.registerClass(Qr_code_map_render.class);
		ConfigurationSerialization.registerClass(Gif_map_render.class);
		
		ConfigurationSerialization.registerClass(Background_map_render.class);
		ConfigurationSerialization.registerClass(Analog_clock_background_map_render.class);
	}

	public MapView create_new_map(World world, Custom_map_render content_render, Custom_map_render edge_render) {
		MapView mapview = Bukkit.getServer().createMap(world);
		replace_render(mapview, content_render, edge_render);
		if (content_render instanceof ConfigurationSerializable) {
			this.set_content_to_save(mapview.getId(), content_render);
		}
		if (edge_render instanceof ConfigurationSerializable) {
			this.set_edge_to_save(mapview.getId(), edge_render);
		}
		return mapview;
	}

	@Nullable
	public MapRenderer get_content_from_map(int id) {
		MapView map = this.custom_map_map.get(id);
		if (map == null) {
			return null;
		}
		List<MapRenderer> renders = map.getRenderers();
		return renders.get(0);
	}

	@Nullable
	public MapRenderer get_edge_from_map(int id) {
		MapView map = this.custom_map_map.get(id);
		if (map == null) {
			return null;
		}
		List<MapRenderer> renders = map.getRenderers();
		return renders.get(1);
	}

	public void set_content_to_save(int id, Custom_map_render content_render) {
		this.set("map_" + id + ".content", content_render);
	}

	public void set_edge_to_save(int id, Custom_map_render edge_render) {
		this.set("map_" + id + ".edge", edge_render);
	}

	public Custom_map_render get_content_from_config(int id) {
		return (Custom_map_render) this.config.get("map_" + id + ".content");
	}

	public Custom_map_render get_edge_from_config(int id) {
		return (Custom_map_render) this.config.get("map_" + id + ".edge");
	}

	public void reload() {
		this.load();
		for (Entry<Integer, MapView> entry : this.custom_map_map.entrySet()) {
			int id = entry.getKey();
			MapView map = entry.getValue();
			Custom_map_render content_render = this.get_content_from_config(id);
			Custom_map_render edge_render = this.get_edge_from_config(id);
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
		if (content_render == null) {
			content_render = new Empty_map_render();
		}
		if (edge_render == null) {
			edge_render = new Empty_map_render();
		}
		map.addRenderer(content_render);
		map.addRenderer(edge_render);
		if (content_render instanceof Custom_map_render) {
			this.custom_map_map.put(map.getId(), map);
		}
	}

}
