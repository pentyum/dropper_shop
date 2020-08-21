package com.piggest.minecraft.bukkit.custom_map;

import org.bukkit.Color;

import java.awt.*;

public class Stock_panel_screen extends Dynamic_string_screen {

	public Stock_panel_screen(Background_map_render background, Font font, int font_size, Color font_color, int width_n, int height_n) {
		super(background, font, font_size, font_color, width_n, height_n);
	}

	@Override
	public String get_current_string() {
		return null;
	}

	@Override
	public int get_refresh_interval() {
		return 5 * 20;
	}
}
