package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Color_utils;

public class Character_map_render extends Static_image_map_render implements ConfigurationSerializable {
	protected char character;
	protected String font_name;
	protected org.bukkit.Color font_color;
	protected int font_size;
	protected Background_map_render background;

	public static void draw_mid_string(Graphics2D g, String str, Font font, int pic_size, int font_size) {
		font = font.deriveFont(Font.PLAIN, font_size);
		g.setFont(font);
		FontRenderContext context = g.getFontRenderContext();
		LineMetrics lineMetrics = font.getLineMetrics(str, context);
		FontMetrics fm = g.getFontMetrics(font);
		int textWidth = fm.stringWidth(str);
		float offset = (pic_size - textWidth) / 2;
		float y = (pic_size + lineMetrics.getAscent() - lineMetrics.getDescent() - lineMetrics.getLeading()) / 2;

		g.drawString(str, offset, y);
	}

	public static BufferedImage char_to_image(Background_map_render background, char character, Font font,
			int font_size, org.bukkit.Color font_color) {
		int side_amount = Character_section_map_render.get_side_amount(font_size);
		int pic_size = Custom_map_render.pic_size * side_amount;

		BufferedImage bi = background.get_image(pic_size, pic_size);
		Graphics2D g = bi.createGraphics();

		java.awt.Color awt_font_color = Color_utils.bukkit_to_awt(font_color);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(awt_font_color);

		draw_mid_string(g, String.valueOf(character), font, pic_size, font_size);

		g.dispose();
		return bi;
	}

	public Character_map_render(Background_map_render background, char character, Font font, int font_size,
			org.bukkit.Color font_color) {
		this.background = background;
		BufferedImage bi = char_to_image(background, character, font, font_size, font_color);
		this.character = character;
		this.image = bi;
		this.font_color = font_color;
		this.font_size = font_size;
		this.font_name = font.getPSName();
	}

	@Override
	public @Nonnull Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<String, Object>();
		save.put("character", this.character);
		save.put("font-color", this.font_color);
		save.put("font-name", this.font_name);
		save.put("font-size", this.font_size);
		save.put("background", this.background);
		save.put("locked", this.locked);
		return save;
	}

	public static Character_map_render deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color font_color = (org.bukkit.Color) args.get("font-color");
		char character = ((String) args.get("character")).charAt(0);
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		int font_size = (int) args.get("font-size");
		Background_map_render background = (Background_map_render) args.get("background");
		boolean locked = (boolean) args.get("locked");
		Character_map_render new_render = new Character_map_render(background, character, font, font_size, font_color);
		new_render.locked = locked;
		return new_render;
	}

}
