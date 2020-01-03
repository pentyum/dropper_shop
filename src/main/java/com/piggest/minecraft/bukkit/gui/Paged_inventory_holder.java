package com.piggest.minecraft.bukkit.gui;

import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface Paged_inventory_holder extends InventoryHolder {
	
	default public void init(int last_slot, int indicator_slot, int next_slot) {
		ItemStack last_item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
		ItemStack indicator_item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemStack next_item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
		ItemMeta last_meta = last_item.getItemMeta();
		ItemMeta indicator_meta = indicator_item.getItemMeta();
		ItemMeta next_meta = next_item.getItemMeta();
		last_meta.setDisplayName("§r上一页");
		indicator_meta.setDisplayName("§r第1页");
		next_meta.setDisplayName("§r下一页");
		last_item.setItemMeta(last_meta);
		indicator_item.setItemMeta(indicator_meta);
		next_item.setItemMeta(next_meta);
		this.getInventory().setItem(last_slot, last_item);
		this.getInventory().setItem(indicator_slot, indicator_item);
		this.getInventory().setItem(next_slot, next_item);
		this.set_gui_page(1);
	}
	
	public int get_gui_page();

	public void set_gui_page(int page);

	public int get_page_size();
	
	public int get_next_button_slot();
	
	public int get_last_button_slot();
	
	public int get_page_indicator_slot();
}
