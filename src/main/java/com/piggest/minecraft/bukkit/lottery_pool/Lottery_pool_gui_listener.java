package com.piggest.minecraft.bukkit.lottery_pool;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.piggest.minecraft.bukkit.grinder.Grinder;

public class Lottery_pool_gui_listener implements Listener {
	@EventHandler
	public void on_press_item(InventoryClickEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Inventory gui = event.getInventory();
		InventoryHolder holder = gui.getHolder();
		if (holder instanceof Lottery_pool_gui_holder) {
			Lottery_pool_gui_holder gui_holder = (Lottery_pool_gui_holder) holder;
			HumanEntity player = event.getWhoClicked();
			int slot = event.getSlot();
			int id = slot + (gui_holder.get_gui_page() - 1) * gui_holder.get_page_size();
			if (slot >= 0 && slot < 27 && player.hasPermission("lottery.set")) {
				if (!Grinder.is_empty(gui.getItem(slot))) {
					player.sendMessage("输入指令/lottery set " + id + " <新的概率> 进行修改");
				}
			}
			if (slot != Lottery_pool_gui_holder.last_slot && slot != Lottery_pool_gui_holder.last_slot) {
				event.setCancelled(true);
			}
		}
	}
}
