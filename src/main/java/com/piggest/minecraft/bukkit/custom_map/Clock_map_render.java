package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Clock_map_render extends Dynamic_string_map_render implements ConfigurationSerializable {

	protected String format = "HH:mm:ss";

	public Clock_map_render(org.bukkit.Color background_color, String format, Font font, int font_size,
			org.bukkit.Color font_color) {
		super(background_color, font, font_size, font_color);
		this.format = format;
	}

	@Override
	public String get_current_string() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		super.render(map, canvas, player);
	}

	@Override
	public @Nonnull Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<String, Object>();
		save.put("format", this.format);
		save.put("background-color", this.background_color);
		save.put("font-color", this.font_color);
		save.put("font-name", this.font_name);
		save.put("font-size", this.font_size);
		return save;
	}

	public static Clock_map_render deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color background_color = (org.bukkit.Color) args.get("background-color");
		org.bukkit.Color font_color = (org.bukkit.Color) args.get("font-color");
		String format = ((String) args.get("format"));
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		int font_size = (int) args.get("font-size");
		Clock_map_render new_render = new Clock_map_render(background_color, format, font, font_size, font_color);
		return new_render;
	}
}
