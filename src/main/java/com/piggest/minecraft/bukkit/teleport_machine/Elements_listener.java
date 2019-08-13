package com.piggest.minecraft.bukkit.teleport_machine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Elements_listener implements Listener {
	@EventHandler
	public void on_click_elements_gui(InventoryClickEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Inventory gui = event.getInventory();
		InventoryHolder holder = event.getInventory().getHolder();
		if (holder instanceof Elements_container) {
			Inventory elements_gui = ((Elements_container) holder).get_elements_gui();
			if (gui == elements_gui) {
				event.setCancelled(true);
			}
		}
	}
}
