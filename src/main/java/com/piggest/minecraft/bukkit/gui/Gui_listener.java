package com.piggest.minecraft.bukkit.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.util.concurrent.Service.Listener;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;

public class Gui_listener extends Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		if (event.getClickedInventory() == null) {
			return;
		}
		String gui_name = event.getClickedInventory().getName();
		String class_name = Dropper_shop_plugin.instance.get_gui_class_name(gui_name);
		Class<?> structure_class = Class.forName(gui_name);
		structure_class
		int slot = event.getSlot();
		Gui_slot_type slot_type = structure.get_locked_slots().get(slot);
		if (slot_type != null) {
			if (slot_type == Gui_slot_type.Indicator) {

			} else if (slot_type == Gui_slot_type.Switch) {
				ItemStack item = event.getCurrentItem();
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				String info = lore.get(0);
				if (info.equals("§r开启")) {
					lore.set(0, "§r关闭");
				} else {
					lore.set(0, "§r开启");
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
			} else if (slot_type == Gui_slot_type.Button) {
				ItemStack item = event.getCurrentItem();
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				lore.add(event.getWhoClicked().getName());
				meta.setLore(lore);
				item.setItemMeta(meta);
			}
			event.setCancelled(true);
			return;
		}
	}
}