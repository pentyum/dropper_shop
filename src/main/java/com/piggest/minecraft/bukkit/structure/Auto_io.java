package com.piggest.minecraft.bukkit.structure;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.util.Vector;

public interface Auto_io {
	public Block get_block(int relative_x, int relative_y, int relative_z);

	default Hopper get_hopper(int[][] check_list, boolean default_face) {
		for (int[] relative_coord : check_list) {
			Block block = this.get_block(relative_coord[0], relative_coord[1], relative_coord[2]);
			synchronized (block) {
				BlockState blockstate = block.getState();
				if (blockstate instanceof Hopper) {
					org.bukkit.block.data.type.Hopper hopper_data = (org.bukkit.block.data.type.Hopper) block
							.getBlockData();
					if (default_face == true) {
						Vector vec = hopper_data.getFacing().getDirection().multiply(2)
								.add(new Vector(relative_coord[0], relative_coord[1], relative_coord[2]));
						if (vec.getBlockX() == 0 && vec.getBlockZ() == 0) {
							if (block.isBlockPowered()) {
								continue;
							}
							return (Hopper) blockstate;
						}
					} else {
						if (block.isBlockPowered()) {
							continue;
						}
						return (Hopper) blockstate;
					}
				}
			}

		}
		return null;
	}

	default Chest get_chest(int[][] check_list) {
		for (int[] relative_coord : check_list) {
			Block block = this.get_block(relative_coord[0], relative_coord[1], relative_coord[2]);
			synchronized (block) {
				BlockState blockstate = block.getState();
				if (blockstate instanceof Chest) {
					return (Chest) blockstate;
				}
			}

		}
		return null;
	}
}
