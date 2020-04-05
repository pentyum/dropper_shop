package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

public class Analog_clock_map_render extends Digital_clock_map_render {

	public Analog_clock_map_render(Color background_color, Font font, int font_size, Color font_color,
			String world_name) {
		super(background_color, "HH:mm:ss", font, font_size, font_color, world_name);
	}
	
	@Override
	public void refresh(MapView map, MapCanvas canvas, Player player) {
		image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = image.createGraphics();

		g.setBackground(awt_background_color);
		g.clearRect(0, 0, 128, 128);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(awt_font_color);
		//Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		//Character_map_render.draw_mid_string(g, str_cache, font, 128, font_size);

		g.dispose();
		canvas.drawImage(0, 0, image);
	}
}
