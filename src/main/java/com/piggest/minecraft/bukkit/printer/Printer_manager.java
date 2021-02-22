package com.piggest.minecraft.bukkit.printer;

import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import javax.annotation.Nonnull;

public class Printer_manager extends Gui_structure_manager<Printer> {
	private Material[][][] model = {
			{{Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK},
					{Material.IRON_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK},
					{Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK}},
			{{Material.IRON_BLOCK, Material.GLASS, Material.IRON_BLOCK},
					{Material.GLASS, Material.CARTOGRAPHY_TABLE, Material.GLASS},
					{Material.IRON_BLOCK, Material.GLASS, Material.IRON_BLOCK}},
			{{Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK},
					{Material.IRON_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK},
					{Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK}}};
	private int center_x = 1;
	private int center_y = 1;
	private int center_z = 1;

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

	@Nonnull
	@Override
	public String get_permission_head() {
		return "printer";
	}

	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		return new int[]{this.center_x, this.center_y, this.center_z};
	}

}
