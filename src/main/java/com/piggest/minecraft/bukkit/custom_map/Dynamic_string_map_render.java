package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Color_utils;

public abstract class Dynamic_string_map_render extends Custom_map_render {
	protected BufferedImage image;
	protected String str_cache;
	protected String font_name;
	protected org.bukkit.Color font_color;
	protected java.awt.Color awt_font_color;
	protected int font_size;
	protected Background_map_render background;

	public Dynamic_string_map_render(Background_map_render background, Font font, int font_size,
			org.bukkit.Color font_color) {
		BufferedImage bi = new BufferedImage(Custom_map_render.pic_size, Custom_map_render.pic_size,
				BufferedImage.TYPE_INT_ARGB);
		this.image = bi;
		this.font_color = font_color;
		this.font_size = font_size;
		this.font_name = font.getPSName();
		this.background = background;
		awt_font_color = Color_utils.bukkit_to_awt(font_color);
	}

	public abstract String get_current_string();

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		String current_string = this.get_current_string();
		if (current_string.equals(this.str_cache)) {
			return;
		}
		this.str_cache = current_string;
		this.refresh(map, canvas);
	}

	public void refresh(MapView map, MapCanvas canvas) {
		image = background.get_image(Custom_map_render.pic_size, Custom_map_render.pic_size);
		Graphics2D g = image.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(awt_font_color);
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		Character_map_render.draw_mid_string(g, str_cache, font, 128, font_size);

		g.dispose();
		Static_image_map_render.draw_image(canvas, 0, 0, image);
	}
}