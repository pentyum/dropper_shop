package com.piggest.minecraft.bukkit.gui;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Gui_listener implements Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		if (event.getClickedInventory() == null) {
			return;
		}
		// String gui_name = event.getClickedInventory().getName();
		// Bukkit.getLogger().info(event.getClickedInventory().getLocation().toString());
		InventoryHolder holder = event.getClickedInventory().getHolder();
		if (holder == null) {
			return;
		}
		Structure_manager<?> structure_manager = Dropper_shop_plugin.instance.get_structure_manager()
				.get(holder.getClass());
		if (structure_manager == null) {
			return;
		}
		if (!(structure_manager instanceof Gui_structure_manager)) {
			return;
		}
		Gui_structure_manager<?> gui_structure_manager = (Gui_structure_manager<?>) structure_manager;
		Multi_block_with_gui structure = (Multi_block_with_gui) holder;
		int slot = event.getSlot();
		Slot_config slot_config = gui_structure_manager.get_locked_slots().get(slot);
		for (int bar : gui_structure_manager.get_process_bar()) {
			if (slot >= bar * 9 && slot <= bar * 9 + 8) {
				event.setCancelled(true);
				return;
			}
		}
		if (slot_config != null) {
			if (slot_config.type == Gui_slot_type.Indicator) {

			} else if (slot_config.type == Gui_slot_type.Switch) {
				ItemStack item = event.getCurrentItem();
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				String info = lore.get(0);
				if (info.equals("§r当前: 开启")) {  //关闭开关
					if (structure.on_switch_pressed((Player) event.getWhoClicked(), slot, false)) {
						lore.set(0, "§r当前: 关闭");
						meta.setLore(lore);
						item.setItemMeta(meta);
					}
				} else {  //打开开关
					if (structure.on_switch_pressed((Player) event.getWhoClicked(), slot, true)) {
						lore.set(0, "§r当前: 开启");
						meta.setLore(lore);
						item.setItemMeta(meta);
					}
				}
			} else if (slot_config.type == Gui_slot_type.Button) {
				structure.on_button_pressed((Player) event.getWhoClicked(), slot);
			}
			event.setCancelled(true);
			return;
		}
	}
}