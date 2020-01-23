package com.piggest.minecraft.bukkit.compressor;

import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;

public class Compressor_io_runner extends Structure_runner {
	private Compressor compressor = null;

	public Compressor_io_runner(Compressor compressor) {
		this.compressor = compressor;
	}

	@Override
	public void run() {
		if (this.compressor.is_loaded() == false) {
			return;
		}
		Hopper solid_hopper = this.compressor.get_reactant_hopper();
		if (solid_hopper != null) {
			for (ItemStack item : solid_hopper.getInventory().getContents()) {
				if (!Grinder.is_empty(item)) {
					if (this.compressor.add_a_raw(item) == true) {
						break;
					}
				}
			}
		}
		Hopper fuel_hopper = this.compressor.get_piston_hopper();
		if (fuel_hopper != null) {
			for (ItemStack item : fuel_hopper.getInventory().getContents()) {
				if (!Grinder.is_empty(item)) {
					if (this.compressor.add_a_piston(item) == true) {
						break;
					}
				}
			}
		}

		Chest product_chest = compressor.get_chest();
		if (product_chest != null) { // 输出固体产品
			Inventory_io.move_item_to_inventoryholder(compressor.getInventory(), Compressor.product_slot,
					product_chest);

		}
	}

	@Override
	public int get_cycle() {
		return 8;
	}

	@Override
	public int get_delay() {
		return 10;
	}

	@Override
	public boolean is_asynchronously() {
		return false;
	}

}
