package com.piggest.minecraft.bukkit.exp_saver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Exp_saver_listener implements Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		if (event.getClickedInventory() == null) {
			return;
		}
		if (event.getClickedInventory().getName().equals("经验存储器")) {
			int slot = event.getSlot();
			if (slot >= 0 && slot <= 8 || Arrays.binarySearch(Exp_saver.get_buttons(), slot) >= 0) {
				if (slot >= 9) {
					ItemStack item = event.getCurrentItem();
					ItemMeta meta = item.getItemMeta();
					List<String> lore = new ArrayList<String>();
					lore.add(event.getWhoClicked().getName());
					meta.setLore(lore);
					item.setItemMeta(meta);
				}
				event.setCancelled(true);
			}
		}
	}
}
