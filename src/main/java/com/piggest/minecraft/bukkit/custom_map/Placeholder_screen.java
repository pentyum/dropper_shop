package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.awt.*;

public class Placeholder_screen extends Dynamic_string_screen {
	private String text;

	public Placeholder_screen(Background_map_render background, String text, Font font, int font_size,
							  Color font_color) {
		super(background, font, font_size, font_color, 1, 1);
		this.text = text;
	}

	@Override
	public String get_current_string() {
		if (Dropper_shop_plugin.instance.use_placeholder()) {
			return PlaceholderAPI.setPlaceholders(null, this.text);
		} else {
			return this.text;
		}
	}

	@Override
	public int get_refresh_interval() {
		return 20;
	}
}
