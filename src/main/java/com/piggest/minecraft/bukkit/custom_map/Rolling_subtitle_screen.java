package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Rolling_subtitle_screen extends String_screen {
	private String str;
	private final float speed;
	private float head_x;

	public Rolling_subtitle_screen(Background_map_render background, String str, Font font, int font_size, org.bukkit.Color font_color, int length_n, float speed) {
		super(background, font, font_size, font_color, length_n, 1);
		this.str = str;
		this.head_x = this.get_show_width();
		this.speed = speed;
	}

	@Override
	public void refresh() {
		if (this.get_tail_x() < 0) {
			this.head_x = this.get_show_width();
		}
		BufferedImage img = background.get_image(this.get_show_width(), this.get_show_height());
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(awt_font_color);
		Character_screen.draw_vertically_mid_string(g, this.str, this.font, this.get_show_height(), this.font_size, this.head_x);
		g.dispose();
		this.head_x -= this.speed;
		this.refresh(img);
	}

	private float get_tail_x() {
		return this.head_x + Character_screen.get_str_width(this.str, this.font, this.font_size);
	}

	@Override
	public int get_refresh_interval() {
		return 4;
	}

	public void set_str(String new_str) {
		this.str = new_str;
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("string", this.str);
		save.put("speed", this.speed);
		return save;
	}

	public float get_speed() {
		return this.speed;
	}

	public static Rolling_subtitle_screen deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color font_color = (org.bukkit.Color) args.get("font-color");
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		int font_size = (int) args.get("font-size");
		Background_map_render background = (Background_map_render) args.get("background");
		String string = (String) args.get("string");
		float speed = (float) args.get("speed");
		boolean locked = (boolean) args.get("locked");
		int length_n = (int) args.get("width-n");
		Rolling_subtitle_screen new_screen = new Rolling_subtitle_screen(background, string, font, font_size, font_color, length_n, speed);
		new_screen.locked = locked;
		return new_screen;
	}
}
