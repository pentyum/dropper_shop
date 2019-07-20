package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Advanced_furnace_temp_runner extends Structure_runner {
	private Advanced_furnace adv_furnace;
	private int cycle = 2;

	public Advanced_furnace_temp_runner(Advanced_furnace adv_furnace) {
		this.adv_furnace = adv_furnace;
	}

	public int get_cycle() {
		return this.cycle;
	}

	public void run() {
		if (this.adv_furnace.is_loaded() == false) {
			return;
		}
		double current_temp = adv_furnace.get_temperature();
		double p = adv_furnace.get_power();
		double loss = adv_furnace.get_power_loss();
		double d_current_temp = p - loss;
		adv_furnace.set_temperature(current_temp + cycle * d_current_temp);

		if (adv_furnace.get_fuel() != null) {
			this.run_fuel();
		} else {
			this.get_fuel();
		}
	}

	private void run_fuel() {
		int max_ticks = (int) (adv_furnace.get_fuel().ticks * adv_furnace.get_time_modify());
		int last_sec = (max_ticks - adv_furnace.fuel_ticks) / 20;
		adv_furnace.set_last_sec(last_sec);
		if (adv_furnace.fuel_ticks >= max_ticks) {
			adv_furnace.set_fuel(null);
			adv_furnace.fuel_ticks = 0;
			return;
		}
		adv_furnace.fuel_ticks += cycle;
	}

	private void get_fuel() {
		ItemStack fuel_item = adv_furnace.get_gui_item(Advanced_furnace.fuel_slot);
		if (!Grinder.is_empty(fuel_item)) {
			Fuel fuel = Fuel.get_fuel(fuel_item);
			if (fuel != null) {
				if (fuel == Fuel.lava_bucket) {
					ItemStack bucket = new ItemStack(Material.BUCKET);
					if (!this.adv_furnace.add_a_item_to_slot(bucket, Advanced_furnace.fuel_product_slot)) {
						return;
					}
				}
				fuel_item.setAmount(fuel_item.getAmount() - 1);
				adv_furnace.set_fuel(fuel);
			}
		}
	}

	@Override
	public int get_delay() {
		return 10;
	}

	@Override
	public boolean is_asynchronously() {
		return true;
	}
}
