package com.piggest.minecraft.bukkit.utils;

public class Color_utils {
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
