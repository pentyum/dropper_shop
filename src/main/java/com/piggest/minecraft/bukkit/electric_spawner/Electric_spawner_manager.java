package com.piggest.minecraft.bukkit.electric_spawner;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;

public class Electric_spawner_manager extends Gui_structure_manager<Electric_spawner> {
	private Material[][][] model = {
			{ { Material.CHISELED_QUARTZ_BLOCK, Material.BOOKSHELF, Material.CHISELED_QUARTZ_BLOCK },
					{ Material.BOOKSHELF, Material.DIAMOND_BLOCK, Material.BOOKSHELF },
					{ Material.CHISELED_QUARTZ_BLOCK, Material.BOOKSHELF, Material.CHISELED_QUARTZ_BLOCK } },
			{ { Material.IRON_BARS, Material.IRON_BARS, Material.IRON_BARS }, 
					{ Material.IRON_BARS, Material.BEACON, Material.IRON_BARS },
					{ Material.IRON_BARS, Material.IRON_BARS, Material.IRON_BARS } },
			{ { Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_QUARTZ_STAIRS },
					{ Material.SMOOTH_QUARTZ_STAIRS, Material.DIAMOND_BLOCK, Material.SMOOTH_QUARTZ_STAIRS },
					{ Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_QUARTZ_STAIRS } },
			{ { null, null, null },
					{ null, Material.ENCHANTING_TABLE, null },
					{ null, null, null } }};
	private int[] center = new int[] {1, 1, 1};
	
	public Electric_spawner_manager() {
		super(Electric_spawner.class);
		this.set_gui(10, null, "raw-slot-1", Gui_slot_type.Item_store);
		this.set_gui(11, null, "raw-slot-2", Gui_slot_type.Item_store);
		this.set_gui(12, null, "raw-slot-3", Gui_slot_type.Item_store);
		this.set_gui(13, null, "raw-slot-4", Gui_slot_type.Item_store);
		this.set_gui(14, null, "raw-slot-5", Gui_slot_type.Item_store);
		this.set_gui(15, null, "raw-slot-6", Gui_slot_type.Item_store);
		this.set_gui(16, null, "raw-slot-7", Gui_slot_type.Item_store);
		this.set_gui(Electric_spawner.info_indicator_slot, Material.SPAWNER, "§e刷怪信息", Gui_slot_type.Indicator);
		this.set_gui(Electric_spawner.synthesis_button_slot, Material.PISTON, "§e合成", Gui_slot_type.Button);
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
		return 18;
	}

	@Override
	public int[] get_process_bar() {
		return new int[] { 0 };
	}

	@Override
	public String get_permission_head() {
		return "electric_spawner";
	}

	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		return this.center;
	}

}
