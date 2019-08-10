package com.piggest.minecraft.bukkit.exp_saver;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;

public class Exp_saver_io_runner extends Structure_runner {
	Exp_saver exp_saver = null;

	public Exp_saver_io_runner(Exp_saver exp_saver) {
		this.exp_saver = exp_saver;
	}

	@Override
	public void run() {
		if (exp_saver.is_loaded() == false) {
			return;
		}
		Hopper hopper = exp_saver.get_hopper();
		if (hopper != null) {
			org.bukkit.block.data.type.Hopper hopper_data = (org.bukkit.block.data.type.Hopper) hopper.getBlockData();
			if (hopper_data.getFacing() == BlockFace.DOWN) {
				for (ItemStack item : hopper.getInventory().getContents()) {
					if (item != null && item.getType() != Material.AIR) {
						Inventory_io.move_a_item_to_slot(item, exp_saver.getInventory(), Exp_saver.mending_slot);
					}
				}
			}
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
