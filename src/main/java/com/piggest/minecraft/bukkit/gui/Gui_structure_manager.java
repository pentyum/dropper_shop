package com.piggest.minecraft.bukkit.gui;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public abstract class Gui_structure_manager<T extends Multi_block_with_gui> extends Structure_manager<T> {
	private HashMap<Integer, Slot_config> locked_slots = new HashMap<Integer, Slot_config>();
	public static final int[] NO_BAR = new int[0];

	public Gui_structure_manager(Class<T> structure_class) {
		super(structure_class);
	}
	
	public void set_gui_full_name(int slot, String full_name, String name, Gui_slot_type type) {
		Slot_config slot_config = new Slot_config(full_name, name, type);
		this.locked_slots.put(slot, slot_config);
	}
	
	public void set_gui(int slot, Material material, String name, Gui_slot_type type) {
		Slot_config slot_config = new Slot_config(material, name, type);
		this.locked_slots.put(slot, slot_config);
	}
	
	
	public void set_gui(int slot, Material material, String name, String[] lore, Gui_slot_type type) {
		Slot_config slot_config = new Slot_config(material, name, lore, type);
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

	public abstract int[] get_process_bar();
	
	public void remove(T structure) {
		super.remove(structure);
	}
}
