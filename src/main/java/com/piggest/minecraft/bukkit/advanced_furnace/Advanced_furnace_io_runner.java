package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;

public class Advanced_furnace_io_runner extends Structure_runner {
	private Advanced_furnace adv_furnace;

	public Advanced_furnace_io_runner(Advanced_furnace advanced_furnace) {
		this.adv_furnace = advanced_furnace;
	}

	@Override
	public void run() {
		if (this.adv_furnace.is_loaded() == false) {
			return;
		}
		Hopper solid_hopper = this.adv_furnace.get_solid_reactant_hopper();
		if (solid_hopper != null) {
			for (ItemStack item : solid_hopper.getInventory().getContents()) {
				if (!Grinder.is_empty(item)) {
					if (this.adv_furnace.add_a_solid(item)) {
						break;
					}
				}
			}
		}
		Hopper fuel_hopper = this.adv_furnace.get_fuel_hopper();
		if (fuel_hopper != null) {
			for (ItemStack item : fuel_hopper.getInventory().getContents()) {
				if (!Grinder.is_empty(item)) {
					if (this.adv_furnace.add_a_fuel(item) == true) {
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

		this.adv_furnace.set_litting(this.adv_furnace.get_temperature() > 1200);

		if (this.adv_furnace.get_temperature() > 4000) {
			Location loc = this.adv_furnace.get_location();
			loc.getWorld().createExplosion(loc, 8);
			this.adv_furnace.remove();
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
