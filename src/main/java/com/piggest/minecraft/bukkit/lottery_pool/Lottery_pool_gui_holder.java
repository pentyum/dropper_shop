package com.piggest.minecraft.bukkit.lottery_pool;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.gui.Paged_inventory_holder;

public class Lottery_pool_gui_holder implements Paged_inventory_holder {
	public static final int page_size = 27;
	public static final int last_slot = 27;
	public static final int indicator_slot = 31;
	public static final int next_slot = 35;
	private int current_page = 1;
	private Inventory gui;

	public Lottery_pool_gui_holder() {
		int current_price = Dropper_shop_plugin.instance.get_price_config().get_lottery_price();
		this.gui = Bukkit.createInventory(this, 36, "奖品列表 - 当前抽奖费用: " + current_price);
		this.init(last_slot, indicator_slot, next_slot);
	}

	@Override
	public Inventory getInventory() {
		return this.gui;
	}

	@Override
	public int get_gui_page() {
		return this.current_page;
	}

	@Override
	public void set_gui_page(int page) {
		if (page <= 0) {
			return;
		}
		YamlConfiguration config = Dropper_shop_plugin.instance.get_lottery_config().get_config();
		@SuppressWarnings("unchecked")
		List<ItemStack> item_list = (List<ItemStack>) config.getList("pool");
		List<Integer> possibility_list = config.getIntegerList("possibility");
		List<Boolean> broadcast_list = config.getBooleanList("broadcast");
		int total_pages = (item_list.size() - 1) / page_size + 1;
		// msg += "\n------------抽奖概率公示 第" + String.format("%2d /%2d", page,
		// total_pages) + " 页------------\n";
		int total = 0;
		int gui_index = -1;
		for (int i = 0; i < item_list.size(); i++) {
			ItemStack item = item_list.get(i);
			int possibility = possibility_list.get(i);
			if (i >= page_size * (page - 1) && i < page_size * page) {
				ItemStack show_item = item.clone();
				ItemMeta meta = show_item.getItemMeta();
				if (meta != null) {
					List<String> lore;
					if (!meta.hasLore()) {
						lore = new ArrayList<String>();
					} else {
						lore = meta.getLore();
					}
					lore.add("§r概率:" + String.format("%4.1f", (float) possibility / 10) + "%");
					lore.add("§r播报:" + broadcast_list.get(i));
					meta.setLore(lore);
					show_item.setItemMeta(meta);
				}
				gui_index = i - (page - 1) * page_size;
				this.getInventory().setItem(gui_index, show_item);
			}
			total += possibility;
		}
		for (int i = gui_index + 1; i < page_size; i++) {
			this.getInventory().setItem(i, null);
		}
		ItemStack indicator_item = this.getInventory().getItem(this.get_page_indicator_slot());
		ItemMeta meta = indicator_item.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§7总中奖概率: " + String.format("%5.1f", (float) total / 10) + "%");
		lore.add("§7总页数: " + total_pages);
		meta.setLore(lore);
		indicator_item.setItemMeta(meta);
		this.current_page = page;
	}

	@Override
	public int get_page_size() {
		return page_size;
	}

	@Override
	public int get_next_button_slot() {
		return next_slot;
	}

	@Override
	public int get_last_button_slot() {
		return last_slot;
	}

	@Override
	public int get_page_indicator_slot() {
		return indicator_slot;
	}

}
