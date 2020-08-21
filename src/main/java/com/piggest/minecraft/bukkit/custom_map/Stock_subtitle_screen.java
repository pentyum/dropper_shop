package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.stock.Sina_stock;
import org.bukkit.Color;

import javax.annotation.Nonnull;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Stock_subtitle_screen extends Rolling_subtitle_screen {
	private final List<String> stock_list;
	private int time = 0;

	public Stock_subtitle_screen(Background_map_render background, String[] stock_list, Font font, int font_size, Color font_color, int length_n, float speed) {
		this(background, Arrays.asList(stock_list), font, font_size, font_color, length_n, speed);
	}

	public Stock_subtitle_screen(Background_map_render background, List<String> stock_list, Font font, int font_size, Color font_color, int length_n, float speed) {
		super(background, "Error", font, font_size, font_color, length_n, speed);
		this.stock_list = stock_list;
	}

	private void update_stock_info() {
		StringBuilder stock_str = new StringBuilder();
		String[] stocks_array = new String[stock_list.size()];
		try {
			Sina_stock[] stocks = Sina_stock.get_stock_info(stock_list.toArray(stocks_array));
			for (Sina_stock stock : stocks) {
				stock_str.append(stock.toString());
				stock_str.append("   ");
			}
		} catch (Exception e) {
			stock_str = new StringBuilder(e.getLocalizedMessage());
		}
		this.set_str(stock_str.toString());
	}

	@Override
	public void refresh() {
		if (this.time * this.get_refresh_interval() >= 40) {//两秒更新一次
			this.time = 0;
			this.update_stock_info();
		}
		super.refresh();
		this.time++;
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("stock-list", this.stock_list);
		return save;
	}

	public static Stock_subtitle_screen deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color font_color = (org.bukkit.Color) args.get("font-color");
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		int font_size = (int) args.get("font-size");
		Background_map_render background = (Background_map_render) args.get("background");
		List<String> stock_list = (List<String>) args.get("stock-list");
		float speed = (float) args.get("speed");
		boolean locked = (boolean) args.get("locked");
		int length_n = (int) args.get("width-n");
		Stock_subtitle_screen new_screen = new Stock_subtitle_screen(background, stock_list, font, font_size, font_color, length_n, speed);
		new_screen.locked = locked;
		return new_screen;
	}
}
