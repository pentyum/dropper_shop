package com.piggest.minecraft.bukkit.exp_saver;

import org.bukkit.event.Listener;

public class Exp_saver_listener implements Listener {
	/*
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
			if (slot >= 0 && slot <= 8 || Arrays.binarySearch(Exp_saver.config.get_buttons(), slot) >= 0) {
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
	*/
}
