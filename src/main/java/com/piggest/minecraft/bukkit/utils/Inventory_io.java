package com.piggest.minecraft.bukkit.utils;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.depository.Depository;
import com.piggest.minecraft.bukkit.depository.Reader;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;

public class Inventory_io {
	public static boolean move_item_to_inventoryholder(Inventory inventory, int slot, InventoryHolder holder) {
		synchronized (inventory) {
			synchronized (holder) {
				ItemStack item_to_move = inventory.getItem(slot);
				ItemStack move_item = item_to_move.clone();
				move_item.setAmount(1);
				HashMap<Integer, ItemStack> unaddable = holder.getInventory().addItem(move_item);
				if (unaddable.size() == 0) {
					item_to_move.setAmount(item_to_move.getAmount() - 1);
					return true;
				} else {
					return false;
				}
			}
		}
	}

	public static boolean move_a_item_to_slot(ItemStack src_item, Inventory inventory, int slot) {
		if (!Grinder.is_empty(src_item)) {
			synchronized (src_item) {
				synchronized (inventory) {
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
			}
		}
		return false;
	}

	public static int Item_get_amount(ItemStack item) {
		if (Reader.is_reader(item)) {
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			Location loc = Reader.lore_parse_loction(lore);
			Depository depository = Dropper_shop_plugin.instance.get_depository_manager().get(loc);
			String full_name = Reader.lore_parse_material_str(lore);
			return depository.get_material_num(full_name);
		} else {
			return item.getAmount();
		}
	}

	public static ItemStack Item_remove_one(ItemStack item) {
		if (Grinder.is_empty(item)) {
			return null;
		}
		ItemStack move_item = null;
		if (Reader.is_reader(item)) {
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			Location loc = Reader.lore_parse_loction(lore);
			String id_name = Reader.lore_parse_material_str(lore);
			Depository depository = Dropper_shop_plugin.instance.get_depository_manager().get(loc);
			move_item = depository.remove(id_name);
			Reader.item_lore_update(item);
		} else {
			move_item = item.clone();
			move_item.setAmount(1);
			item.setAmount(item.getAmount() - 1);
		}
		return move_item;
	}

	public static boolean is_empty(ItemStack item) {
		if (item == null) {
			return true;
		}
		if (item.getType() == Material.AIR) {
			return true;
		}
		if (Reader.is_reader(item)) {
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			return Reader.lore_parse_num(lore) == 0;
		}
		return false;
	}
}
