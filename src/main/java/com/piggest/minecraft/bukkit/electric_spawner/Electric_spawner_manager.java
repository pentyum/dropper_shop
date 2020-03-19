package com.piggest.minecraft.bukkit.electric_spawner;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;

public class Electric_spawner_manager extends Gui_structure_manager<Electric_spawner> {
	private Material[][][] model = {
			{ { Material.CHISELED_STONE_BRICKS, Material.BOOKSHELF, Material.CHISELED_STONE_BRICKS },
					{ Material.BOOKSHELF, Material.DIAMOND_BLOCK, Material.BOOKSHELF },
					{ Material.CHISELED_STONE_BRICKS, Material.BOOKSHELF, Material.CHISELED_STONE_BRICKS } },
			{ { Material.IRON_BARS, Material.IRON_BARS, Material.IRON_BARS }, 
					{ Material.IRON_BARS, Material.BEACON, Material.IRON_BARS },
					{ Material.IRON_BARS, Material.IRON_BARS, Material.IRON_BARS } },
			{ { Material.BRICK_STAIRS, Material.BRICK_STAIRS, Material.BRICK_STAIRS },
					{ Material.BRICK_STAIRS, Material.DIAMOND_BLOCK, Material.BRICK_STAIRS },
					{ Material.BRICK_STAIRS, Material.BRICK_STAIRS, Material.BRICK_STAIRS } },
			{ { null, null, null },
					{ null, Material.ENCHANTING_TABLE, null },
					{ null, null, null } }};
	private int[] center = new int[] {1, 1, 1};
	
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
		return this.model;
	}

	@Override
	public int[] get_center() {
		return this.center;
	}

}
