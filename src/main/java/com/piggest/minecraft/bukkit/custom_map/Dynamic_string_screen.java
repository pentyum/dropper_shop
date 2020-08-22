package com.piggest.minecraft.bukkit.custom_map;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Dynamic_string_screen extends String_screen {
	protected String str_cache = null;


	public Dynamic_string_screen(Background_map_render background, Font font, int font_size,
								 org.bukkit.Color font_color, int width_n, int height_n) {
		super(background, font, font_size, font_color, width_n, height_n);
	}

	public abstract String get_current_string();

	public void refresh() {
		if (this.get_current_string().equals(this.str_cache)) {
			return;
		}
		this.str_cache = this.get_current_string();
		BufferedImage img = background.get_image(this.get_show_width(), this.get_show_height());

		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.draw_in_background(g);
		g.dispose();
		this.refresh(img);
	}

	protected void draw_in_background(Graphics2D g) {
		g.setColor(awt_font_color);
		Character_screen.draw_central_string(g, str_cache, font, this.get_show_width(), this.get_show_height(), font_size);
	}
}
