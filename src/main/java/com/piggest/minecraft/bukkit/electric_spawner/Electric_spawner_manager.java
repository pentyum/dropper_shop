package com.piggest.minecraft.bukkit.electric_spawner;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;

public class Electric_spawner_manager extends Gui_structure_manager<Electric_spawner> {

	public Electric_spawner_manager() {
		super(Electric_spawner.class);
	}

	@Override
	public String get_gui_name() {
		return "魔力刷怪笼";
	}

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.CHEST;
	}

	@Override
	public int get_slot_num() {
		return 9;
	}

	@Override
	public int[] get_process_bar() {
		return null;
	}

	@Override
	public String get_permission_head() {
		return "electric_spawner";
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
