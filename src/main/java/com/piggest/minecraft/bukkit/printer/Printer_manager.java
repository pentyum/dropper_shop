package com.piggest.minecraft.bukkit.printer;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;

public class Printer_manager extends Gui_structure_manager<Printer> {

	public Printer_manager() {
		super(Printer.class);
	}

	@Override
	public String get_gui_name() {
		return "打印机";
	}

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.CHEST;
	}

	@Override
	public int get_slot_num() {
		return 27;
	}

	@Override
	public int[] get_process_bar() {
		return null;
	}

	@Override
	public String get_permission_head() {
		return "printer";
	}

	@Override
	public Material[][][] get_model() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public int[] get_center() {
		// TODO 自动生成的方法存根
		return null;
	}

}
