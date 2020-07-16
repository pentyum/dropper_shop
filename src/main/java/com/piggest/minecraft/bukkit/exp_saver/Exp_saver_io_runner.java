package com.piggest.minecraft.bukkit.exp_saver;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;

public class Exp_saver_io_runner extends Structure_runner {
	public Exp_saver_io_runner(Exp_saver_manager manager) {
		super(manager);
	}

	@Override
	public void run_instance(Structure structure) {
		Exp_saver exp_saver = (Exp_saver) structure;
		if (exp_saver.is_loaded() == false) {
			return;
		}
		Hopper hopper = exp_saver.get_hopper();
		if (hopper != null) {
			org.bukkit.block.data.type.Hopper hopper_data = (org.bukkit.block.data.type.Hopper) hopper.getBlockData();
			if (hopper_data.getFacing() == BlockFace.DOWN) {
				for (ItemStack item : hopper.getInventory().getContents()) {
					if (!Grinder.is_empty(item)) {
						Inventory_io.move_item_to_slot(item, 1, exp_saver.getInventory(), Exp_saver.mending_slot);
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


}
