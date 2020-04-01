package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Fonts_manager {
	private final HashMap<String, Font> font_map = new HashMap<>();

	public void register_font(String file_name) throws FontFormatException, IOException {
		File font_file = new File(Dropper_shop_plugin.instance.getDataFolder(), "./fonts/" + file_name);
		Font new_font = Font.createFont(Font.TRUETYPE_FONT, font_file);
		font_map.put(new_font.getPSName(), new_font);
	}

	public void register_font(File font_file) throws FontFormatException, IOException {
		Font new_font = Font.createFont(Font.TRUETYPE_FONT, font_file);
		String name = new_font.getPSName();
		font_map.put(name, new_font);
		Dropper_shop_plugin.instance.getLogger().info("成功注册字体" + name);
	}

	public Set<String> get_all_name() {
		return font_map.keySet();
	}

	public Font get_font(String font_name) {
		return font_map.get(font_name);
	}

	private static class Font_filter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			if (name.endsWith(".ttc")) {
				return true;
			} else if (name.endsWith(".ttf")) {
				return true;
			} else if (name.endsWith(".TTC")) {
				return true;
			} else if (name.endsWith(".TTF")) {
				return true;
			}
			return false;
		}

	}

	public void register_all_fonts() {
		File font_folder = new File(Dropper_shop_plugin.instance.getDataFolder(), "fonts");
		for (File file : font_folder.listFiles(new Font_filter())) {
			try {
				register_font(file);
			} catch (FontFormatException | IOException e) {
				Dropper_shop_plugin.instance.getLogger().warning(file.getName() + "字体错误");
			}
		}
	}
}
