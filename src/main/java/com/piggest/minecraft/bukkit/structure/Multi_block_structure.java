package com.piggest.minecraft.bukkit.structure;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class Multi_block_structure extends Structure {

	public abstract void on_right_click(Player player);
	
	public Block get_block(int relative_x, int relative_y, int relative_z) {
		Location loc = this.get_location().add(relative_x, relative_y, relative_z);
		return loc.getBlock();
	}

	public abstract int completed();

	public abstract boolean in_structure(Location loc);

}
