package com.piggest.minecraft.bukkit.gui;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public abstract class Gui_config {
	private HashMap<Integer, Slot_config> locked_slots = new HashMap<Integer, Slot_config>();

	public void set_gui(int slot, Material material, String name, Gui_slot_type type) {
		/*
		 * ItemStack item = new ItemStack(material); Grinder.set_item_name(item, name);
		 * this.getInventory().setItem(i, item);
		 */
		Slot_config slot_config = new Slot_config(material, name, type);
		this.locked_slots.put(slot, slot_config);
	}

	public Slot_config get_config(int slot) {
		return this.locked_slots.get(slot);
	}

	public HashMap<Integer, Slot_config> get_locked_slots() {
		return this.locked_slots;
	}
	
	public abstract String get_gui_name();
	public abstract InventoryType get_inventory_type();
	public abstract int get_slot_num(); 
}
