package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Color;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Character_section_map_render extends Character_map_render {
	private int section;

	public static int get_side_amount(int font_size) {
		return font_size / Character_map_render.pic_size + 1;
	}

	public static BufferedImage get_section_of_image(BufferedImage bi, int side_amount, int section) {
		int start_y = (section / side_amount) * Character_map_render.pic_size;
		int start_x = (section % side_amount) * Character_map_render.pic_size;
		return bi.getSubimage(start_x, start_y, Character_map_render.pic_size, Character_map_render.pic_size);
	}

	public Character_section_map_render(Color background_color, char character, Font font, int font_size,
			Color font_color, int section) {
		super(background_color, character, font, font_size, font_color);
		int side_amount = get_side_amount(font_size);
		this.image = get_section_of_image(this.image, side_amount, section);
		this.section = section;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("section", this.section);
		return save;
	}

	public static Character_section_map_render deserialize(@Nonnull Map<String, Object> args) {
		Color background_color = (Color) args.get("background-color");
		Color font_color = (Color) args.get("font-color");
		char character = ((String) args.get("character")).charAt(0);
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		int font_size = (int) args.get("font-size");
		int section = (int) args.get("section");
		Character_section_map_render new_render = new Character_section_map_render(background_color, character, font,
				font_size, font_color, section);
		return new_render;
	}
}
