package com.piggest.minecraft.bukkit.depository;

import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.gui.Gui_config;

public class Depository_config extends Gui_config {

	@Override
	public String get_gui_name() {
		return "存储器";
	}

	@Override
	public int get_slot_num() {
		return 0;
	}

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.HOPPER;
	}
	
}
