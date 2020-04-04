package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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
import com.piggest.minecraft.bukkit.utils.Color_utils;

public class Clock_map_render extends Custom_map_render implements ConfigurationSerializable {
	protected BufferedImage image;
	protected String format = "HH:mm:ss";
	protected String font_name;
	protected org.bukkit.Color background_color;
	protected org.bukkit.Color font_color;
	protected java.awt.Color awt_background_color;
	protected java.awt.Color awt_font_color;
	protected int font_size;
	protected String str_cache;

	public Clock_map_render(org.bukkit.Color background_color, String format, Font font, int font_size,
			org.bukkit.Color font_color) {
		BufferedImage bi = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		this.image = bi;

		this.format = format;
		this.font_color = font_color;
		this.background_color = background_color;
		this.font_size = font_size;
		this.font_name = font.getPSName();

		awt_background_color = Color_utils.bukkit_to_awt(background_color);
		awt_font_color = Color_utils.bukkit_to_awt(font_color);
	}

	public String get_current_string() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		String current_time_string = this.get_current_string();
		if (current_time_string.equals(this.str_cache)) {
			return;
		}
		this.str_cache = current_time_string;

		image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = image.createGraphics();

		g.setBackground(awt_background_color);
		g.clearRect(0, 0, 128, 128);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(awt_font_color);
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		Character_map_render.draw_mid_string(g, str_cache, font, 128, font_size);

		g.dispose();
		canvas.drawImage(0, 0, image);
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
