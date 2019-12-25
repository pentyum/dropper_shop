package com.piggest.minecraft.bukkit.lottery_pool;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.gui.Paged_inventory;
import com.piggest.minecraft.bukkit.gui.Paged_inventory_holder;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Lottery_pool_manager extends Structure_manager<Lottery_pool> implements Paged_inventory_holder {
	public static Lottery_pool_manager instance = null;

	public Lottery_pool_manager() {
		super(Lottery_pool.class);
		Lottery_pool_manager.instance = this;
	}

	/*
	 * @Override public Lottery_pool find(String player_name, Location loc, boolean
	 * new_structure) { if (new_structure == false) { return this.get(loc); } else {
	 * Lottery_pool lottery_pool = new Lottery_pool();
	 * lottery_pool.set_location(loc); if (lottery_pool.completed() == false) {
	 * return null; } else { return lottery_pool; } } }
	 */

	@Override
	public Lottery_pool find_existed(Location loc) {
		return this.get(loc);
	}

	@Override
	public Lottery_pool find_and_make(Player player, Location loc) {
		Lottery_pool lottery_pool = new Lottery_pool();
		lottery_pool.set_location(loc);
		if (lottery_pool.completed() == false) {
			return null;
		} else {
			return lottery_pool;
		}
	}

	@Override
	public String get_permission_head() {
		return "lottery";
	}

	@Override
	public Material[][][] get_model() {
		return null;
	}

	@Override
	public int[] get_center() {
		return null;
	}

	@Override
	public Paged_inventory getInventory() {
		int current_price = Dropper_shop_plugin.instance.get_price_config().get_lottery_price();
		Paged_inventory gui = new Paged_inventory(this, 36, "奖品列表 - 当前抽奖费用: " + current_price);
		this.set_gui_page(gui, 1);
		return gui;
	}

	@Override
	public int get_gui_page(Paged_inventory paged_inventory) {
		return 1;
	}

	@Override
	public void set_gui_page(Paged_inventory paged_inventory, int page) {
		FileConfiguration config = Dropper_shop_plugin.instance.get_lottery_config();
		@SuppressWarnings("unchecked")
		List<ItemStack> item_list = (List<ItemStack>) config.getList("pool");
		List<Integer> possibility_list = config.getIntegerList("possibility");
		List<Boolean> broadcast_list = config.getBooleanList("broadcast");
		int page_size = 27;
		int total_pages = (item_list.size() - 1) / page_size + 1;
		String msg = "";
		msg += "\n------------抽奖概率公示 第" + String.format("%2d /%2d", page, total_pages) + " 页------------\n";
		int total = 0;
		for (int i = 0; i < item_list.size(); i++) {
			ItemStack item = item_list.get(i);
			int possibility = possibility_list.get(i);
			if (i >= page_size * (page - 1) && i < page_size * page) {
				ItemStack show_item = item.clone();
				if (show_item.hasItemMeta()) {
					ItemMeta meta = show_item.getItemMeta();
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
				paged_inventory.setItem(i - (page - 1) * page_size, show_item);
			}
			total += possibility;
		}
		msg += "总中奖概率" + String.format("%5.1f", (float) total / 10) + "%";
	}
}
