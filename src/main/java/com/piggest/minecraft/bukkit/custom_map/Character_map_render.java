package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Color_utils;

public class Character_map_render extends MapRenderer implements ConfigurationSerializable {
	private char character;
	private BufferedImage image;
	private String font_name;
	private org.bukkit.Color background_color;
	private org.bukkit.Color font_color;
	private int font_size;
	public static final int pic_size = 128;

	public Character_map_render(org.bukkit.Color background_color, char character, Font font,
			org.bukkit.Color font_color) {
		BufferedImage bi = new BufferedImage(pic_size, pic_size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		java.awt.Color awt_background_color = Color_utils.bukkit_to_awt(background_color);
		java.awt.Color awt_font_color = Color_utils.bukkit_to_awt(font_color);

		g.setBackground(awt_background_color);
		g.clearRect(0, 0, pic_size, pic_size);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int size = 100;
		g.setColor(awt_font_color);
		font = font.deriveFont(Font.PLAIN, size);
		g.setFont(font);

		String str = String.valueOf(character);

		FontRenderContext context = g.getFontRenderContext();
		LineMetrics lineMetrics = font.getLineMetrics(str, context);

		float offset = (pic_size - size) / 2;
		float y = (pic_size + lineMetrics.getAscent() - lineMetrics.getDescent() - lineMetrics.getLeading()) / 2;

		g.drawString(str, offset, y);
		g.dispose();

		this.character = character;
		this.image = bi;
		this.font_color = font_color;
		this.background_color = background_color;
		this.font_size = size;
		this.font_name = font.getPSName();
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		canvas.drawImage(0, 0, image);
	}

	@Override
	public void initialize(MapView map) {
		super.initialize(map);
		map.setLocked(true);
	}

	@Override
	public @Nonnull Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<String, Object>();
		save.put("character", this.character);
		save.put("background-color", this.background_color);
		save.put("font-color", this.font_color);
		save.put("font-name", this.font_name);
		save.put("font-size", this.font_size);
		return save;
	}

	public static Character_map_render deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color background_color = (org.bukkit.Color) args.get("background-color");
		org.bukkit.Color font_color = (org.bukkit.Color) args.get("font-color");
		char character = ((String) args.get("character")).charAt(0);
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		int size = (int) args.get("font-size");
		Character_map_render new_render = new Character_map_render(background_color, character, font, font_color);
		return new_render;
	}
}
