package com.piggest.minecraft.bukkit.grinder;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Grinder_io_runner extends Structure_runner {
	public Grinder_io_runner(Grinder_manager manager) {
		super(manager);
	}

	public void run_instance(Structure structure) {
		Grinder grinder = (Grinder) structure;
		if (grinder.is_loaded() == false) {
			return;
		}
		Hopper hopper = grinder.get_hopper();
		Chest chest = grinder.get_chest();
		if (hopper != null) {
			org.bukkit.block.data.type.Hopper hopper_data = (org.bukkit.block.data.type.Hopper) hopper.getBlockData();
			if (hopper_data.getFacing() == BlockFace.DOWN) {
				Inventory hopper_inv = hopper.getInventory();
				for (ItemStack item : hopper_inv.getContents()) {
					if (Grinder.is_empty(item)) {
						continue;
					}
					if (grinder.get_manager().get_main_product(item.getType()) != null) {
						if (grinder.add_a_raw(item) == true) {
							break;
						}
					}
					if (Dropper_shop_plugin.instance.get_flint_unit(item.getType()) != 0) {
						if (grinder.add_a_flint(item) == true) {
							break;
						}
					}
				}
			}
		}
		if (chest != null) {
			Inventory_io.move_item_to_inventoryholder(grinder.getInventory(), 13, chest);
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


}
