package com.piggest.minecraft.bukkit.compressor;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;

public class Compressor_manager extends Gui_structure_manager<Compressor> {
	private Material[][][] model = {
			{ { Material.STONE_BRICKS, Material.IRON_BLOCK, Material.STONE_BRICKS },
				{ Material.IRON_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK },
				{ Material.STONE_BRICKS, Material.IRON_BLOCK, Material.STONE_BRICKS } },
			{ { Material.IRON_BLOCK, Material.PISTON, Material.IRON_BLOCK },
				{ Material.PISTON, null, Material.PISTON },
				{ Material.IRON_BLOCK, Material.PISTON, Material.IRON_BLOCK } },
			{ { Material.STONE_BRICKS, Material.IRON_BLOCK, Material.STONE_BRICKS },
				{ Material.IRON_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK },
				{ Material.STONE_BRICKS, Material.IRON_BLOCK, Material.STONE_BRICKS } } };;
	private String gui_name = "压缩机";
	private int[] center = new int[] {1,1,1};
	
	public Compressor_manager() {
		super(Compressor.class);
		this.set_gui(10, Material.BLUE_STAINED_GLASS_PANE, "§r左边放原料", Gui_slot_type.Indicator);
		this.set_gui(12, Material.BLUE_STAINED_GLASS_PANE, "§r左边放活塞", Gui_slot_type.Indicator);
		this.set_gui(14, Material.BLUE_STAINED_GLASS_PANE, "§r左边为产品", Gui_slot_type.Indicator);
		this.set_gui(Compressor.raw_slot, null, "raw-slot", Gui_slot_type.Item_store);
		this.set_gui(Compressor.piston_slot, null, "piston-slot", Gui_slot_type.Item_store);
		this.set_gui(Compressor.product_slot, null, "main-product", Gui_slot_type.Item_store);
		this.set_gui(16, Material.BLUE_STAINED_GLASS_PANE, "§r右边为活塞单元储量", Gui_slot_type.Indicator);
		this.set_gui(17, Material.FLINT, "§e活塞单元", Gui_slot_type.Indicator);
	}

	@Override
	public String get_gui_name() {
		return this.gui_name;
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
		return "compressor";
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
