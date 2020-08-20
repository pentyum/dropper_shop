package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Color_utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Dynamic_string_screen extends Screen {
	protected String str_cache;
	protected String font_name;
	protected org.bukkit.Color font_color;
	protected java.awt.Color awt_font_color;
	protected int font_size;
	protected Background_map_render background;

	public Dynamic_string_screen(Background_map_render background, Font font, int font_size,
								 org.bukkit.Color font_color, int width_n, int height_n, Fill_type fill_type) {
		super(width_n, height_n, fill_type);
		this.font_color = font_color;
		this.font_size = font_size;
		this.font_name = font.getPSName();
		this.background = background;
		awt_font_color = Color_utils.bukkit_to_awt(font_color);
	}

	public abstract String get_current_string();

	public void refresh() {
		if (this.get_current_string().equals(this.str_cache)) {
			return;
		}
		this.str_cache = this.get_current_string();
		BufferedImage img = background.get_image(Custom_map_render.pic_size, Custom_map_render.pic_size);
		Graphics2D g = img.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(awt_font_color);
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		Character_map_render.draw_mid_string(g, str_cache, font, 128, font_size);

		g.dispose();
		this.refresh(img);
	}
}
