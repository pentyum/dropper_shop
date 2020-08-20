package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Screen_map_render extends Custom_map_render implements ConfigurationSerializable {
	private final int section;
	private final int screen_id;
	private long time = 0;

	public Screen_map_render(int section, int screen_id) {
		this.section = section;
		this.screen_id = screen_id;
	}


	@Override
	public void refresh(MapView map, MapCanvas canvas) {
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<>();
		save.put("section", this.section);
		save.put("screen-id", this.screen_id);
		return save;
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		Screen screen = this.get_screen();
		if (screen == null) {
			return;
		}
		long upstream_time = screen.time;
		if (upstream_time <= this.time) {
			return;
		}
		BufferedImage img = screen.get_section(this.section);
		Static_image_map_render.draw_image(canvas, 0, 0, img);
		this.time = upstream_time;
	}

	public int get_section() {
		return this.section;
	}

	public int get_x() {
		return this.section % this.get_screen().get_show_width_n();
	}

	public int get_y() {
		return this.section / this.get_screen().get_show_width_n();
	}

	public Screen get_screen() {
		return Dropper_shop_plugin.instance.get_screen_config().get_screen(this.screen_id);
	}

	public static Screen_map_render deserialize(Map<String, Object> save) {
		int section = (int) save.get("section");
		int screen_id = (int) save.get("screen-id");
		return new Screen_map_render(section, screen_id);
	}

}
