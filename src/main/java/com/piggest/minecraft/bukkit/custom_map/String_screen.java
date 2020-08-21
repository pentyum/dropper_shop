package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.utils.Color_utils;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Map;

public abstract class String_screen extends Screen {
	protected Font font;
	protected String font_name;
	protected org.bukkit.Color font_color;
	protected java.awt.Color awt_font_color;
	protected int font_size;
	protected Background_map_render background;

	public String_screen(Background_map_render background, Font font, int font_size,
						 org.bukkit.Color font_color, int width_n, int height_n) {
		super(width_n, height_n, Fill_type.NONE);
		this.font_color = font_color;
		this.font_size = font_size;
		this.font = font;
		this.font_name = font.getPSName();
		this.background = background;
		this.awt_font_color = Color_utils.bukkit_to_awt(font_color);
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("font-color", this.font_color);
		save.put("font-name", this.font_name);
		save.put("font-size", this.font_size);
		save.put("background", this.background);
		return save;
	}
}
