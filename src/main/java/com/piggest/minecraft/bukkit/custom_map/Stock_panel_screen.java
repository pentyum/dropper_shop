package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.stock.Sina_stock;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class Stock_panel_screen extends Dynamic_string_screen {
	private final String stock_id;
	private Sina_stock stock_cache;
	private final int title_size;
	private final int title_y;
	private final int rise_size;
	private final int rise_y;

	public Stock_panel_screen(Background_map_render background, String stock_id, Font font, int size_n) {
		super(background, font, size_n * Custom_map_render.pic_size / 5, org.bukkit.Color.WHITE, size_n, size_n);
		this.stock_id = stock_id;
		this.title_size = size_n * Custom_map_render.pic_size / 5;
		this.title_y = size_n * Custom_map_render.pic_size / 4;
		this.rise_size = size_n * Custom_map_render.pic_size / 7;
		this.rise_y = size_n * (Custom_map_render.pic_size - Custom_map_render.pic_size / 5);
	}

	@Override
	public String get_current_string() {
		try {
			this.stock_cache = Sina_stock.get_stock_info_fast(stock_id);
			return String.format("%.2f", stock_cache.get_share_price());
		} catch (InterruptedException | IOException | URISyntaxException | IndexOutOfBoundsException e) {
			String err_msg = e.toString();
			Dropper_shop_plugin.instance.getLogger().warning("[股市面板]" + err_msg);
			return err_msg;
		}
	}


	@Override
	public int get_refresh_interval() {
		return 5 * 20;
	}

	@Override
	protected void draw_in_background(Graphics2D g) {
		g.setColor(awt_font_color);
		Character_screen.draw_mid_string(g, this.stock_cache.get_name(), this.font, this.get_show_width(), this.title_size, this.title_y);
		if (this.stock_cache.get_rise_value() > 0) {//涨
			g.setColor(Color.RED);
		} else if (this.stock_cache.get_rise_value() < 0) {//跌
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.WHITE);
		}
		Character_screen.draw_central_string(g, str_cache, this.font, this.get_show_width(), this.get_show_height(), font_size);
		String rise_str = String.format("%.2f  %.2f%%", Math.abs(this.stock_cache.get_rise_value()), Math.abs(this.stock_cache.get_rise_percent()));
		Character_screen.draw_mid_string(g, rise_str, this.font, this.get_show_width(), this.rise_size, rise_y);
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("stock-id", this.stock_id);
		return save;
	}

	public static Stock_panel_screen deserialize(@Nonnull Map<String, Object> args) {
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		Background_map_render background = (Background_map_render) args.get("background");
		String stock_id = (String) args.get("stock-id");
		boolean locked = (boolean) args.get("locked");
		int size_n = (int) args.get("width-n");
		Stock_panel_screen new_screen = new Stock_panel_screen(background, stock_id, font, size_n);
		new_screen.locked = locked;
		return new_screen;
	}
}
