package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Server_date;

public class Digital_clock_map_render extends Dynamic_string_map_render implements ConfigurationSerializable {
	protected String world_name = null;
	protected String format = "HH:mm:ss";
	protected Calendar date;

	/*
	 * public Digital_clock_map_render(Map<String, Object> args) {
	 * this((Background_map_render) args.get("background"), (String)
	 * args.get("format"),
	 * Dropper_shop_plugin.instance.get_fonts_manager().get_font((String)
	 * args.get("font-name")), (int) args.get("font-size"), (org.bukkit.Color)
	 * args.get("font-color"), (String) args.get("world")); }
	 */

	public Digital_clock_map_render(Background_map_render background, String format, Font font, int font_size,
			org.bukkit.Color font_color, String world_name) {
		super(background, font, font_size, font_color);
		this.format = format;
		this.format = this.format.replace('S', '_');
		if (world_name != null) {
			if (!world_name.equalsIgnoreCase("null")) {
				this.world_name = world_name;
				this.format = this.format.replace('s', '_');
			}
		}
	}

	@Override
	public String get_current_string() {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (this.world_name == null) {
			this.date = Calendar.getInstance();
			return sdf.format(date.getTime());
		} else {
			World world = Bukkit.getWorld(world_name);
			if (world != null) {
				this.date = Server_date.get_world_date(world);
				return sdf.format(date.getTime());
			}
		}
		return format;
	}

	@Override
	public @Nonnull Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<String, Object>();
		save.put("format", this.format);
		save.put("font-color", this.font_color);
		save.put("font-name", this.font_name);
		save.put("font-size", this.font_size);
		save.put("world", this.world_name);
		save.put("background", this.background);
		return save;
	}

	public static Digital_clock_map_render deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color font_color = (org.bukkit.Color) args.get("font-color");
		String format = (String) args.get("format");
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		int font_size = (int) args.get("font-size");
		String world_name = (String) args.get("world");
		Background_map_render background = (Background_map_render) args.get("background");
		Digital_clock_map_render new_render = new Digital_clock_map_render(background, format, font, font_size,
				font_color, world_name);
		return new_render;
	}

}
