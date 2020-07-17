package com.piggest.minecraft.bukkit.advanced_furnace;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Old_structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;

public class Advanced_furnace_io_runner extends Old_structure_runner {

	public Advanced_furnace_io_runner(Advanced_furnace_manager manager) {
		super(manager);
	}

	@Override
	public void run_instance(Structure structure) {
		Advanced_furnace adv_furnace = (Advanced_furnace) structure;

		if (adv_furnace.is_loaded() == false) {
			return;
		}
		Hopper solid_hopper = adv_furnace.get_solid_reactant_hopper();
		if (solid_hopper != null) {
			for (ItemStack item : solid_hopper.getInventory().getContents()) {
				if (!Grinder.is_empty(item)) {
					if (adv_furnace.add_a_solid(item)) {
						break;
					}
				}
			}
		}
		Hopper fuel_hopper = adv_furnace.get_fuel_hopper();
		if (fuel_hopper != null) {
			for (ItemStack item : fuel_hopper.getInventory().getContents()) {
				if (!Grinder.is_empty(item)) {
					if (adv_furnace.add_a_fuel(item) == true) {
						break;
					}
				}
			}
		}

		Chest product_chest = adv_furnace.get_chest();
		if (product_chest != null) { // 输出产品
			Inventory_io.move_item_to_inventoryholder(adv_furnace.getInventory(), Advanced_furnace.solid_product_slot,
					product_chest);

		}

		adv_furnace.set_litting(adv_furnace.get_temperature() > 1200);

		if (adv_furnace.get_temperature() > 4000) {
			Location loc = adv_furnace.get_location();
			loc.getWorld().createExplosion(loc, 8);
			adv_furnace.remove();
		}
	}

	@Override
	public boolean is_asynchronously() {
		return false;
	}

	@Override
	public int get_cycle() {
		return 8;
	}

	@Override
	public int get_delay() {
		return 10;
	}

}
