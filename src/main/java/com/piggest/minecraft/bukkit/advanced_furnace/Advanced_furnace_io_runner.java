package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Advanced_furnace_io_runner extends Structure_runner {
	private Advanced_furnace adv_furnace;

	public Advanced_furnace_io_runner(Advanced_furnace advanced_furnace) {
		this.adv_furnace = advanced_furnace;
	}

	@Override
	public void run() {
		if (this.adv_furnace.get_location().getChunk().isLoaded() == false) {
			return;
		}
		int solid_check_list[][] = { { 0, 1, 2 }, { 2, 1, 0 }, { 0, 1, -2 }, { -2, 1, 0 } };  //注入固体
		for (int[] relative_coord : solid_check_list) {
			BlockState block = this.adv_furnace.get_block(relative_coord[0], relative_coord[1], relative_coord[2])
					.getState();
			if (block instanceof Hopper) {
				org.bukkit.block.data.type.Hopper hopper_data = (org.bukkit.block.data.type.Hopper) block
						.getBlockData();
				Vector vec = hopper_data.getFacing().getDirection().multiply(2)
						.add(new Vector(relative_coord[0], relative_coord[1], relative_coord[2]));
				if (vec.getBlockX() == 0 && vec.getBlockZ() == 0) {
					Hopper hopper = (Hopper) block;
					for (ItemStack item : hopper.getInventory().getContents()) {
						if (item != null && item.getType() != Material.AIR) {
							this.adv_furnace.add_a_solid(item);
						}
					}
				}
			}
		}
		int[][] fuel_check_list = { { 0, -1, 2 }, { 2, -1, 0 }, { 0, -1, -2 }, { -2, -1, 0 } };  //注入燃料
		for (int[] relative_coord : fuel_check_list) {
			BlockState block = this.adv_furnace.get_block(relative_coord[0], relative_coord[1], relative_coord[2])
					.getState();
			if (block instanceof Hopper) {
				org.bukkit.block.data.type.Hopper hopper_data = (org.bukkit.block.data.type.Hopper) block
						.getBlockData();
				Vector vec = hopper_data.getFacing().getDirection().multiply(2)
						.add(new Vector(relative_coord[0], relative_coord[1], relative_coord[2]));
				if (vec.getBlockX() == 0 && vec.getBlockZ() == 0) {
					Hopper hopper = (Hopper) block;
					for (ItemStack item : hopper.getInventory().getContents()) {
						if (item != null && item.getType() != Material.AIR) {
							this.adv_furnace.add_a_fuel(item);
						}
					}
				}
			}
		}
	}

	@Override
	public int get_cycle() {
		return 2;
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
