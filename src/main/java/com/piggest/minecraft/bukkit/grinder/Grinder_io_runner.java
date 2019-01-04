package com.piggest.minecraft.bukkit.grinder;

import java.util.HashMap;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Grinder_io_runner extends Structure_runner {
	private Grinder grinder;

	public Grinder_io_runner(Grinder grinder) {
		this.grinder = grinder;
	}

	public void run() {
		Hopper hopper = grinder.get_hopper();
		Chest chest = grinder.get_chest();
		if (this.grinder.get_location().getChunk().isLoaded() == false) {
			return;
		}
		if (hopper != null) {
			org.bukkit.block.data.type.Hopper hopper_data = (org.bukkit.block.data.type.Hopper) hopper.getBlockData();
			if (hopper_data.getFacing() == BlockFace.DOWN) {
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
		}
		if (chest != null) {
			if (!Grinder.is_empty(grinder.get_product())) {
				ItemStack move_item = grinder.get_product().clone();
				move_item.setAmount(1);
				HashMap<Integer, ItemStack> unaddable = chest.getInventory().addItem(move_item);
				if (unaddable.size() == 0) {
					grinder.get_product().setAmount(grinder.get_product().getAmount() - 1);
				}
			}
		}
	}

	@Override
	public int get_cycle() {
		return 10;
	}

	@Override
	public int get_delay() {
		return 10;
	}

}
