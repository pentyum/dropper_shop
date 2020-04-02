package com.piggest.minecraft.bukkit.utils;

import java.util.HashMap;

public class Color_utils {
	public static final HashMap<String, org.bukkit.Color> string_color_map = new HashMap<String, org.bukkit.Color>() {
		private static final long serialVersionUID = -1025588471080787905L;

		{
			put("WHITE", org.bukkit.Color.WHITE);
			put("BLACK", org.bukkit.Color.BLACK);
			put("BLUE", org.bukkit.Color.BLUE);
			put("FUCHSIA", org.bukkit.Color.FUCHSIA);
			put("GRAY", org.bukkit.Color.GRAY);
			put("GREEN", org.bukkit.Color.GREEN);
			put("LIME", org.bukkit.Color.LIME);
			put("MAROON", org.bukkit.Color.MAROON);
			put("NAVY", org.bukkit.Color.NAVY);
			put("OLIVE", org.bukkit.Color.OLIVE);
			put("ORANGE", org.bukkit.Color.ORANGE);
			put("PURPLE", org.bukkit.Color.PURPLE);
			put("RED", org.bukkit.Color.RED);
			put("SILVER", org.bukkit.Color.SILVER);
			put("TEAL", org.bukkit.Color.TEAL);
			put("YELLOW", org.bukkit.Color.YELLOW);
		}
	};

	public static java.awt.Color bukkit_to_awt(org.bukkit.Color bukkit_color) {
		int r = bukkit_color.getRed();
		int g = bukkit_color.getGreen();
		int b = bukkit_color.getBlue();
		return new java.awt.Color(r, g, b);
	}

	public static org.bukkit.Color awt_to_bukkit(java.awt.Color awt_color) {
		int r = awt_color.getRed();
		int g = awt_color.getGreen();
		int b = awt_color.getBlue();
		return org.bukkit.Color.fromRGB(r, g, b);
	}

}
