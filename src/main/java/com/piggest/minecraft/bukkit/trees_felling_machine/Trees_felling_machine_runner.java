package com.piggest.minecraft.bukkit.trees_felling_machine;

import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Trees_felling_machine_runner extends Structure_runner {
	private Trees_felling_machine machine = null;

	public Trees_felling_machine_runner(Trees_felling_machine machine) {
		this.machine = machine;
	}

	@Override
	public void run() {
		if (!machine.get_location().getChunk().isLoaded()) {
			return;
		}
		int solid_check_list[][] = { { 0, 1, 2 }, { 2, 1, 0 }, { 0, 1, -2 }, { -2, 1, 0 } }; // 注入固体
		for (int[] relative_coord : solid_check_list) {
			BlockState block = this.machine.get_block(relative_coord[0], relative_coord[1], relative_coord[2])
					.getState();
			if (block instanceof Hopper) {
				org.bukkit.block.data.type.Hopper hopper_data = (org.bukkit.block.data.type.Hopper) block
						.getBlockData();
				Vector vec = hopper_data.getFacing().getDirection().multiply(2)
						.add(new Vector(relative_coord[0], relative_coord[1], relative_coord[2]));
				if (vec.getBlockX() == 0 && vec.getBlockZ() == 0) {
					Hopper hopper = (Hopper) block;
					if (hopper.getBlock().isBlockPowered()) {
						continue;
					}
					for (ItemStack item : hopper.getInventory().getContents()) {
						if (!Grinder.is_empty(item)) {
							this.machine.add_a_axe(item);
						}
					}
				}
			}
		}
		if (this.machine.is_working()) {
			this.machine.do_next();
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
