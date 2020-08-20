package com.piggest.minecraft.bukkit.custom_map.clock;

import com.piggest.minecraft.bukkit.custom_map.Background_map_render;
import com.piggest.minecraft.bukkit.custom_map.Dynamic_string_screen;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Server_date;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class Digital_clock_screen extends Dynamic_string_screen implements ConfigurationSerializable {
	protected String world_name = null;
	protected String format = "HH:mm:ss";
	protected Calendar date;

	public Digital_clock_screen(Background_map_render background, String format, Font font, int font_size,
								org.bukkit.Color font_color, String world_name) {
		super(background, font, font_size, font_color, 1, 1, Fill_type.FULL);
		this.format = format;
		this.format = this.format.replace('S', '_');
		if (world_name != null) {
			if (!world_name.equalsIgnoreCase("null")) {
				this.world_name = world_name;
				this.format = this.format.replace('s', '_');
			}
		}
		this.refresh();
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

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("format", this.format);
		save.put("font-color", this.font_color);
		save.put("font-name", this.font_name);
		save.put("font-size", this.font_size);
		save.put("world", this.world_name);
		save.put("background", this.background);
		save.put("locked", this.locked);
		return save;
	}

	@Override
	public int get_refresh_interval() {
		return 10;
	}

	public static Digital_clock_screen deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color font_color = (org.bukkit.Color) args.get("font-color");
		String format = (String) args.get("format");
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		int font_size = (int) args.get("font-size");
		String world_name = (String) args.get("world");
		Background_map_render background = (Background_map_render) args.get("background");
		boolean locked = (boolean) args.get("locked");
		Digital_clock_screen new_render = new Digital_clock_screen(background, format, font, font_size,
				font_color, world_name);
		new_render.locked = locked;
		return new_render;
	}

}
