package com.piggest.minecraft.bukkit.structure;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class Multi_block_structure extends Structure {

	protected abstract void on_right_click(Player player);


	/**
	 * 判定该结构是否完整
	 */
	public boolean completed() {
		Structure_manager<? extends Structure> manager = this.get_manager();
		Material[][][] model = manager.get_model();
		int[] center = manager.get_center();
		if (model != null) {
			for (int y = 0; y < model.length; y++) { // 从第0层开始
				for (int z = 0; z < model[y].length; z++) {
					for (int x = 0; x < model[y][z].length; x++) {
						Block check_block = this.get_block(x - center[0], y - center[1], z - center[2]);
						if (model[y][z][x] != null) {
							if (check_block.getType() != model[y][z][x]) {
								return false;
							}
							Structure existed = manager.find_existed(check_block.getLocation());
							if (existed != null) {
								if (!existed.get_location().equals(this.get_location())) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 判定某个位置是否在该结构内部
	 */
	public boolean in_structure(Location loc) {
		int[] center = this.get_manager().get_center();
		int r_x = loc.getBlockX() - this.x + center[0];
		int r_y = loc.getBlockY() - this.y + center[1];
		int r_z = loc.getBlockZ() - this.z + center[2];
		try {
			if (this.get_manager().get_model()[r_y][r_z][r_x] != null) {
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}

}
