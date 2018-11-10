package com.piggest.minecraft.bukkit.depository;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class Update_component_listener implements Listener {
	@EventHandler
	public void on_update(CraftItemEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		ItemStack item = event.getRecipe().getResult();
		if (Update_component.is_component(item)) {
			int level = Update_component.get_level(item);
			if (level == 0) {
				return;
			}
			if (!event.getInventory().contains(Update_component.component_item[level - 1])) {
				event.setCancelled(true);
			}
		}
	}
}
