package com.piggest.minecraft.bukkit.teleport_machine;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;

public class Teleport_machine_manager extends Gui_structure_manager<Teleport_machine> {
	private final int[] process_bar = new int[] {0};
	private Material[][][] model = new Material[][][] {
		{{},
		{},
		{}},
		{{},
		{},
		{}},
		{{},
		{},
		{}},
	};

	public Teleport_machine_manager() {
		super(Teleport_machine.class);
		
		this.set_gui(16, Material.RED_STAINED_GLASS_PANE, "增加带宽", Gui_slot_type.Button);
		this.set_gui(17, Material.RED_STAINED_GLASS_PANE, "提高频率", Gui_slot_type.Button);
		
		this.set_gui(25, Material.RED_STAINED_GLASS_PANE, "当前带宽", Gui_slot_type.Indicator);
		this.set_gui(26, Material.RED_STAINED_GLASS_PANE, "当前频率", Gui_slot_type.Indicator);
		
		this.set_gui(34, Material.BLUE_STAINED_GLASS_PANE, "减少带宽", Gui_slot_type.Button);
		this.set_gui(35, Material.BLUE_STAINED_GLASS_PANE, "降低频率", Gui_slot_type.Button);
	}

	@Override
	public String get_permission_head() {
		return "teleport_machine";
	}

	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String get_gui_name() {
		return "传送机";
	}

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.CHEST;
	}

	@Override
	public int get_slot_num() {
		return 36;
	}

	@Override
	public int[] get_process_bar() {
		return process_bar;
	}
}
