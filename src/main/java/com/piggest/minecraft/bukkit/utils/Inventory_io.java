package com.piggest.minecraft.bukkit.utils;

import java.util.HashMap;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.grinder.Grinder;

public class Inventory_io {
	public static boolean move_item_to_inventoryholder(Inventory inventory, int slot, InventoryHolder holder) {
		synchronized (inventory) {
			synchronized (holder) {
				ItemStack item_to_move = inventory.getItem(slot);
				if (!Grinder.is_empty(item_to_move)) {
					ItemStack move_item = item_to_move.clone();
					move_item.setAmount(1);
					HashMap<Integer, ItemStack> unaddable = holder.getInventory().addItem(move_item);
					if (unaddable.size() == 0) {
						item_to_move.setAmount(item_to_move.getAmount() - 1);
						return true;
					}
				}
				return false;
			}
		}
	}
	
	public static boolean move_a_item_to_slot(ItemStack src_item,Inventory inventory, int slot) {
		if (!Grinder.is_empty(src_item)) {
			if (Grinder.is_empty(inventory.getItem(slot))) {
				inventory.setItem(slot, src_item.clone());
				inventory.getItem(slot).setAmount(1);
				src_item.setAmount(src_item.getAmount() - 1);
				return true;
			} else if (src_item.isSimilar(inventory.getItem(slot))) {
				int new_num = 1 + inventory.getItem(slot).getAmount();
				if (new_num <= src_item.getMaxStackSize()) {
					inventory.getItem(slot).setAmount(new_num);
					src_item.setAmount(src_item.getAmount() - 1);
					return true;
				}
			}
		}
		return false;
	}
}
