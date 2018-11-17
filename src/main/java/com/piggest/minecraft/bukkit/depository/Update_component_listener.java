package com.piggest.minecraft.bukkit.depository;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
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
			CraftingInventory inventory = event.getInventory();
			ItemStack basis = inventory.getContents()[5];
			if (!Update_component.is_component(basis)) {
				event.getWhoClicked().sendMessage("必须使用升级组件合成，而你使用的是" + basis.getType().name());
				event.setCancelled(true);
				return;
			}
			if (Update_component.get_level(basis) != level - 1) {
				event.getWhoClicked().sendMessage("必须使用" + (level - 1) + "级升级组件合成" + level + "级升级组件");
				event.setCancelled(true);
				return;
			}
		}
	}
}
