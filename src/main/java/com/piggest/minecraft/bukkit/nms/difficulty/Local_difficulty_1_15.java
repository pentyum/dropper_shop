package com.piggest.minecraft.bukkit.nms.difficulty;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock;

import net.minecraft.server.v1_15_R1.DifficultyDamageScaler;
import net.minecraft.server.v1_15_R1.WorldServer;

public class Local_difficulty_1_15 implements Local_difficulty {
	public float get_local_difficulty(Location loc) {
		CraftWorld world = (CraftWorld) loc.getWorld();
		CraftBlock block = (CraftBlock) loc.getBlock();
		WorldServer world_nms = world.getHandle();
		DifficultyDamageScaler local_difficulty = world_nms.getDamageScaler(block.getPosition());
		return local_difficulty.b();
	}
}
