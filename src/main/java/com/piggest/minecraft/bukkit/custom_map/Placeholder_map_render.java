package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Font;

import org.bukkit.Color;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

import me.clip.placeholderapi.PlaceholderAPI;

public class Placeholder_map_render extends Dynamic_string_map_render {
	private String text;

	public Placeholder_map_render(Background_map_render background, String text, Font font, int font_size,
			Color font_color) {
		super(background, font, font_size, font_color);
		this.text = text;
	}

	@Override
	public String get_current_string(Player player) {
		if (Dropper_shop_plugin.instance.use_placeholder()) {
			return PlaceholderAPI.setPlaceholders(player, this.text);
		} else {
			return this.text;
		}
	}

}
