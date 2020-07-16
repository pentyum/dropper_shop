package com.piggest.minecraft.bukkit.utils;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.type.Lectern;

import java.util.HashSet;

public class Use_block {
	private static final HashSet<Material> use_set = new HashSet<Material>() {
		private static final long serialVersionUID = -80343265520227606L;

		{
			add(Material.ACACIA_FENCE_GATE);
			add(Material.BIRCH_FENCE_GATE);
			add(Material.DARK_OAK_FENCE_GATE);
			add(Material.JUNGLE_FENCE_GATE);
			add(Material.OAK_FENCE_GATE);
			add(Material.SPRUCE_FENCE_GATE);
			add(Material.LEVER);
			add(Material.REPEATER);
			add(Material.COMMAND_BLOCK);
			add(Material.COMPARATOR);
			add(Material.DAYLIGHT_DETECTOR);
			add(Material.CRAFTING_TABLE);
			add(Material.STONECUTTER);
			add(Material.JUKEBOX);
			add(Material.NOTE_BLOCK);
			add(Material.BELL);
			add(Material.GRINDSTONE);
			add(Material.LOOM);
			add(Material.FLETCHING_TABLE);
			add(Material.CARTOGRAPHY_TABLE);
			add(Material.SMITHING_TABLE);
			add(Material.ENCHANTING_TABLE);
			add(Material.BEACON);
			add(Material.ENDER_CHEST);
			add(Material.FLOWER_POT);
		}
	};

	public static boolean is_use_block(Block block) {
		Material type = block.getType();
		if (Tag.DOORS.isTagged(type)) {
			return true;
		}
		if (Tag.TRAPDOORS.isTagged(type)) {
			return true;
		}
		if (Tag.ANVIL.isTagged(type)) {
			return true;
		}
		if (Tag.BEDS.isTagged(type)) {
			return true;
		}
		if (Tag.BUTTONS.isTagged(type)) {
			return true;
		}
		BlockState block_state = block.getState();
		if (block_state instanceof Container) {
			return true;
		}
		if (use_set.contains(type)) {
			return true;
		}
		if (type == Material.LECTERN) {
			Lectern lectern = (Lectern) block.getBlockData();
			return lectern.hasBook();
		}
		return false;
	}
}
