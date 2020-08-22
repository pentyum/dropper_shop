package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Color_utils;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Character_screen extends String_screen {
	protected char character;

	public static int get_side_amount(int font_size) {
		return font_size / Custom_map_render.pic_size + 1;
	}

	public static int get_str_width(String str, Font font, int font_size) {
		font = font.deriveFont(Font.PLAIN, font_size);
		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(font);
		return fm.stringWidth(str);
	}
	/*
	public static void draw_vertically_mid_string(Graphics2D g, String str, Font font, int pic_width, int pic_height, int font_size, float x) {
		font = font.deriveFont(Font.PLAIN, font_size);
		g.setFont(font);
		FontRenderContext context = g.getFontRenderContext();
		LineMetrics lineMetrics = font.getLineMetrics(str, context);
		float y = (pic_height + lineMetrics.getAscent() - lineMetrics.getDescent() - lineMetrics.getLeading()) / 2;

		g.drawString(str, x, y);
	}
	*/

	/**
	 * 绘制垂直居中字符串
	 */
	public static void draw_vertically_mid_string(Graphics2D g, String str, Font font, int pic_height, int font_size, float x) {
		font = font.deriveFont(Font.PLAIN, font_size);
		g.setFont(font);
		FontRenderContext context = g.getFontRenderContext();
		LineMetrics lineMetrics = font.getLineMetrics(str, context);
		float y = (pic_height + lineMetrics.getAscent() - lineMetrics.getDescent() - lineMetrics.getLeading()) / 2;

		g.drawString(str, x, y);
	}

	/**
	 * 绘制居中字符串
	 */
	public static void draw_mid_string(Graphics2D g, String str, Font font, int pic_width, int font_size, float y) {
		font = font.deriveFont(Font.PLAIN, font_size);
		g.setFont(font);
		int textWidth = get_str_width(str, font, font_size);
		float offset = (pic_width - textWidth) / 2f;
		g.drawString(str, offset, y);
	}

	/**
	 * 绘制正中心字符串
	 */
	public static void draw_central_string(Graphics2D g, String str, Font font, int pic_width, int pic_height, int font_size) {
		int textWidth = get_str_width(str, font, font_size);
		float offset = (pic_width - textWidth) / 2f;
		draw_vertically_mid_string(g, str, font, pic_height, font_size, offset);
	}

	public static BufferedImage char_to_image(Background_map_render background, char character, Font font,
											  int font_size, org.bukkit.Color font_color) {
		int side_amount = Character_screen.get_side_amount(font_size);
		int pic_size = Custom_map_render.pic_size * side_amount;

		BufferedImage bi = background.get_image(pic_size, pic_size);
		Graphics2D g = bi.createGraphics();

		java.awt.Color awt_font_color = Color_utils.bukkit_to_awt(font_color);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(awt_font_color);

		draw_central_string(g, String.valueOf(character), font, pic_size, pic_size, font_size);

		g.dispose();
		return bi;
	}

	public Character_screen(Background_map_render background, char character, Font font, int font_size,
							org.bukkit.Color font_color) {
		super(background, font, font_size, font_color, Character_screen.get_side_amount(font_size), Character_screen.get_side_amount(font_size));
		BufferedImage bi = char_to_image(background, character, font, font_size, font_color);
		this.character = character;
		this.raw_img = bi;
		this.refresh();
	}

	@Override
	public @Nonnull
	Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("character", this.character);
		return save;
	}

	@Override
	public void refresh() {
		this.refresh(this.raw_img);
	}

	@Override
	public int get_refresh_interval() {
		return 0;
	}

	public static Character_screen deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color font_color = (org.bukkit.Color) args.get("font-color");
		char character = ((String) args.get("character")).charAt(0);
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		int font_size = (int) args.get("font-size");
		Background_map_render background = (Background_map_render) args.get("background");
		boolean locked = (boolean) args.get("locked");
		Character_screen new_render = new Character_screen(background, character, font, font_size, font_color);
		new_render.locked = locked;
		return new_render;
	}

}
