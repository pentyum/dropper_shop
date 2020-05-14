package com.piggest.minecraft.bukkit.utils;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
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

	public static boolean try_move_item_to_slot(ItemStack src_item, int quantity, Inventory inventory, int slot) {
		if (!Grinder.is_empty(src_item)) {
			synchronized (src_item) {
				if (src_item.getAmount() < quantity) {
					return false;
				}
				synchronized (inventory) {
					if (Grinder.is_empty(inventory.getItem(slot))) {
						return true;
					} else if (src_item.isSimilar(inventory.getItem(slot))) {
						int new_num = quantity + inventory.getItem(slot).getAmount();
						if (new_num <= src_item.getMaxStackSize()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean move_item_to_slot(ItemStack src_item, int quantity, Inventory inventory, int slot) {
		if (!Grinder.is_empty(src_item)) {
			synchronized (src_item) {
				if (src_item.getAmount() < quantity) {
					return false;
				}
				synchronized (inventory) {
					if (Grinder.is_empty(inventory.getItem(slot))) {
						inventory.setItem(slot, src_item.clone());
						inventory.getItem(slot).setAmount(quantity);
						src_item.setAmount(src_item.getAmount() - quantity);
						return true;
					} else if (src_item.isSimilar(inventory.getItem(slot))) {
						int new_num = quantity + inventory.getItem(slot).getAmount();
						if (new_num <= src_item.getMaxStackSize()) {
							inventory.getItem(slot).setAmount(new_num);
							src_item.setAmount(src_item.getAmount() - quantity);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static int item_get_amount(ItemStack item) {
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

	public static ItemStack item_remove(ItemStack item, int quantity) {
		if (Grinder.is_empty(item)) {
			return null;
		}
		ItemStack move_item = null;
		if (Reader.is_reader(item)) {
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			Location loc = Reader.lore_parse_loction(lore);
			String full_name = Reader.lore_parse_material_str(lore);
			Depository depository = Dropper_shop_plugin.instance.get_depository_manager().get(loc);
			if (depository == null) {
				return null;
			}
			move_item = depository.remove(full_name, quantity);
			Reader.item_lore_update(item);
		} else {
			synchronized (item) {
				move_item = item.clone();
				move_item.setAmount(quantity);
				item.setAmount(item.getAmount() - quantity);
			}
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
			Location loc = Reader.lore_parse_loction(lore);
			Depository depository = Dropper_shop_plugin.instance.get_depository_manager().get(loc);
			String full_name = Reader.lore_parse_material_str(lore);
			if (depository == null) {
				return true;
			}
			return depository.get_material_num(full_name) == 0;
		}
		return false;
	}

	public static void give_item_to_player(Player player, ItemStack... items) {
		Location loc = player.getLocation();
		World world = player.getWorld();
		HashMap<Integer, ItemStack> unaddable = player.getInventory().addItem(items);
		for (ItemStack item : unaddable.values()) {
			world.dropItemNaturally(loc, item);
		}
	}
}
