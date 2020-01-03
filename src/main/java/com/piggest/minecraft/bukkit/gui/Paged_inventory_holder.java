package com.piggest.minecraft.bukkit.gui;

import org.bukkit.inventory.InventoryHolder;

public interface Paged_inventory_holder extends InventoryHolder {

	public int get_gui_page();

	public void set_gui_page(int page);

	public int get_page_size();
	
	public int get_next_button_slot();
	
	public int get_last_button_slot();
	
}
