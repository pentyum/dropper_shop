package com.piggest.minecraft.bukkit.lottery_pool;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.piggest.minecraft.bukkit.gui.Paged_inventory;

public class Lottery_pool_gui_listener implements Listener {
	@EventHandler
	public void on_press_item(InventoryClickEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Inventory gui = event.getInventory();
		InventoryHolder holder = gui.getHolder();
		if (holder instanceof Lottery_pool_manager) {
			HumanEntity player = event.getWhoClicked();
			Paged_inventory paged_gui = (Paged_inventory) gui;
			int id = event.getSlot() + (paged_gui.get_current_page() - 1) * paged_gui.get_page_size();
			if (player.hasPermission("lottery.set")) {
				player.sendMessage("输入指令/lottery set " + id + " <新的概率> 进行修改");
			}
			event.setCancelled(true);
		}
	}
}
