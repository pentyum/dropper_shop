package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Grinder_io_runner extends BukkitRunnable {
	private Grinder grinder;

	public Grinder_io_runner(Grinder grinder) {
		this.grinder = grinder;
	}

	public void run() {
		Hopper hopper = grinder.get_hopper();
		Chest chest = grinder.get_chest();
		if (hopper != null) {
			Inventory hopper_inv = hopper.getInventory();
			for (ItemStack item : hopper_inv.getContents()) {
				if (Grinder.is_empty(item)) {
					continue;
				}
				if (Grinder.recipe.get(item.getType()) != null) {
					if (grinder.add_a_raw(item) == true) {
						break;
					}
				}
				if (Dropper_shop_plugin.instance.get_unit(item.getType()) != 0) {
					if (grinder.add_a_flint(item) == true) {
						break;
					}
				}
			}
		}
		if (chest != null) {
			// Grinder.move_a_item(item, grinder.getInventory(), 11);
		}
	}

}
