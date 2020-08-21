package com.piggest.minecraft.bukkit.utils.stock;


import com.mysql.fabric.xmlrpc.base.Array;
import com.piggest.minecraft.bukkit.utils.Http_get;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Sina_stock {
	String id;
	String name;
	float share_price;
	float rise_value;
	float rise_percent;

	public Sina_stock(String id, String name, float share_price, float rise_value, float rise_percent) {
		this.id = id;
		this.name = name;
		this.share_price = share_price;
		this.rise_value = rise_value;
		this.rise_percent = rise_percent;
	}

	/*
	上证：s_sh000001，写作sh000001
	深证：s_sz399001，写作sz399001
	 */
	private static String get_simple_info(String[] stock_ids) throws InterruptedException, IOException, URISyntaxException {
		String[] simplified_stock_list = new String[stock_ids.length];
		Arrays.setAll(simplified_stock_list, i -> "s_" + stock_ids[i]);
		String list = String.join(",", simplified_stock_list);
		return Http_get.get_content("https://hq.sinajs.cn/list=" + list);
	}

	private static Sina_stock[] parse_all_info(String all_info) {
		String[] stock_info = all_info.split(";");
		Sina_stock[] stocks = new Sina_stock[stock_info.length];
		Arrays.setAll(stocks, i -> parse_info(stock_info[i]));
		return stocks;
	}

	private static Sina_stock parse_info(String info) {
		String[] line = info.split("=");
		info = line[1].replaceAll("\"", "");
		String[] info_array = info.split(",");
		String name = info_array[0];
		float share_price = Float.parseFloat(info_array[1]);
		float rise_value = Float.parseFloat(info_array[2]);
		float rise_percent = Float.parseFloat(info_array[3]);
		return new Sina_stock(null, name, share_price, rise_value, rise_percent);
	}

	public static Sina_stock[] get_stock_info(String[] stock_ids) throws InterruptedException, IOException, URISyntaxException {
		String all_info = get_simple_info(stock_ids);
		Sina_stock[] stocks = parse_all_info(all_info);
		Arrays.setAll(stocks, i -> stocks[i].id = stock_ids[i]);
		return stocks;
	}

	@Override
	public String toString() {
		return String.format("%s %.2f %.2f %.2f%%", this.name, this.share_price, this.rise_value, this.rise_percent);
	}
}
