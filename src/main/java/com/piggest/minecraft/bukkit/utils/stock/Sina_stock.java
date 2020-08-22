package com.piggest.minecraft.bukkit.utils.stock;


import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Http_get;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;

public class Sina_stock {
	private static HashMap<String, Sina_stock> stock_cache = new HashMap<>();
	private String id;
	private final String name;
	private final float share_price;
	private final float rise_value;
	private final float rise_percent;
	private final long time;

	public Sina_stock(String id, String name, float share_price, float rise_value, float rise_percent) {
		this.id = id;
		this.name = name;
		this.share_price = share_price;
		this.rise_value = rise_value;
		this.rise_percent = rise_percent;
		this.time = System.currentTimeMillis();
	}

	/*
	上证：s_sh000001，写作sh000001
	深证：s_sz399001，写作sz399001
	 */
	private static String get_simple_info(String... stock_ids) throws InterruptedException, IOException, URISyntaxException {
		String[] simplified_stock_list = new String[stock_ids.length];
		Arrays.setAll(simplified_stock_list, i -> "s_" + stock_ids[i]);
		String list = String.join(",", simplified_stock_list);
		return Http_get.get_content("https://hq.sinajs.cn/list=" + list);
	}

	private static Sina_stock[] parse_all_info(String all_info, int limit) {
		String[] stock_info = all_info.split(";", limit);

		Sina_stock[] stocks = new Sina_stock[stock_info.length];
		Arrays.setAll(stocks, i -> parse_info(stock_info[i]));
		return stocks;
	}

	private static Sina_stock parse_info(String info) {
		try {
			String[] line = info.split("=");
			info = line[1].replaceAll("\"", "");
			String[] info_array = info.split(",");
			String name = info_array[0];
			float share_price = Float.parseFloat(info_array[1]);
			float rise_value = Float.parseFloat(info_array[2]);
			float rise_percent = Float.parseFloat(info_array[3]);
			return new Sina_stock(null, name, share_price, rise_value, rise_percent);
		} catch (Exception e) {
			Dropper_shop_plugin.instance.getLogger().warning(info);
			e.printStackTrace();
			return null;
		}
	}

	public static Sina_stock[] get_stock_info(String... stock_ids) throws InterruptedException, IOException, URISyntaxException {
		String all_info = get_simple_info(stock_ids);
		Sina_stock[] stocks = parse_all_info(all_info, stock_ids.length);
		for (int i = 0; i < stocks.length; i++) {
			stocks[i].id = stock_ids[i];
			Sina_stock.stock_cache.put(stocks[i].id, stocks[i]);
		}
		return stocks;
	}

	public static Sina_stock get_stock_info_fast(String stock_id) throws InterruptedException, IOException, URISyntaxException {
		Sina_stock stock = stock_cache.get(stock_id);
		if (stock == null) {
			return get_stock_info(stock_id)[0];
		}
		if (System.currentTimeMillis() - stock.time > 5 * 1000) {
			return get_stock_info(stock_id)[0];
		}
		return stock;
	}

	@Override
	public String toString() {
		return String.format("%s %.2f %.2f %.2f%%", this.name, this.share_price, this.rise_value, this.rise_percent);
	}

	public float get_share_price() {
		return share_price;
	}

	public float get_rise_value() {
		return rise_value;
	}

	public float get_rise_percent() {
		return rise_percent;
	}

	public String get_id() {
		return this.id;
	}

	public String get_name() {
		return this.name;
	}

}
