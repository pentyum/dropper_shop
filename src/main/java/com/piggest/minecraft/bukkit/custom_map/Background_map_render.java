package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.utils.Color_utils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Background_map_render implements ConfigurationSerializable {
	protected BufferedImage image_cache;
	protected org.bukkit.Color background_color;
	protected java.awt.Color awt_background_color;

	public Background_map_render(org.bukkit.Color background_color) {
		this.background_color = background_color;
		this.awt_background_color = Color_utils.bukkit_to_awt(background_color);
	}

	public java.awt.Color get_background_color() {
		return this.awt_background_color;
	}

	@Override
	public @Nonnull
	Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<String, Object>();
		save.put("background-color", this.background_color);
		return save;
	}

	public static Background_map_render deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color background_color = (org.bukkit.Color) args.get("background-color");
		Background_map_render new_render = new Background_map_render(background_color);
		return new_render;
	}

	public BufferedImage get_image_cache_copy() {
		int w = this.image_cache.getWidth(null);
		int h = this.image_cache.getHeight(null);
		BufferedImage new_img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = new_img.createGraphics();
		g.drawImage(this.image_cache, 0, 0, null);
		g.dispose();
		return new_img;
	}

	public BufferedImage get_image(int pic_size_x, int pic_size_y) {
		BufferedImage bi = new BufferedImage(pic_size_x, pic_size_y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		g.setBackground(awt_background_color);
		g.clearRect(0, 0, pic_size_x, pic_size_y);
		g.dispose();
		this.image_cache = bi;
		return bi;
	}
}
