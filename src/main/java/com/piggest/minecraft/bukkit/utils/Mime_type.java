package com.piggest.minecraft.bukkit.utils;

import java.util.HashMap;

public class Mime_type {
	public static final HashMap<String, String> mime_map = new HashMap<String, String>() {
		private static final long serialVersionUID = 8413155402479869818L;

		{
			put("image/jpeg", ".jpg");
			put("image/png", ".png");
			put("image/gif", ".gif");
			put("image/bmp", ".bmp");
			put("image/webp", ".webp");
			put("image/tiff", ".tiff");

			put("image/svg+xml", ".svg");
			put("image/vnd.adobe.photoshop", ".psd");
			put("application/cdr", ".cdr");
			put("application/pdf", ".pdf");
			put("application/postscript", ".eps");

			put("application/x-font-ttf", ".ttf");
		}
	};
}
