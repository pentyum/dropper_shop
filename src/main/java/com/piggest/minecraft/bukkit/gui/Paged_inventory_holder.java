package com.piggest.minecraft.bukkit.gui;

import org.bukkit.inventory.InventoryHolder;

public interface Paged_inventory_holder extends InventoryHolder {

	public int get_gui_page(Paged_inventory paged_inventory);

	public void set_gui_page(Paged_inventory paged_inventory, int page);

}
