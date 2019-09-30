package com.piggest.minecraft.bukkit.utils;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.type.Lectern;

public class Use_block {
	public static boolean is_use_block(Block block) {
		Material type = block.getType();
		if(Tag.DOORS.isTagged(type)) {
			return true;
		}
		if(Tag.TRAPDOORS.isTagged(type)) {
			return true;
		}
		if(Tag.ANVIL.isTagged(type)) {
			return true;
		}
		if(Tag.BEDS.isTagged(type)) {
			return true;
		}
		if(Tag.BUTTONS.isTagged(type)) {
			return true;
		}
		BlockState block_state = block.getState();
		if(block_state instanceof Container) {
			return true;
		}
		switch (type) {
		case ACACIA_FENCE_GATE:
		case BIRCH_FENCE_GATE:
		case DARK_OAK_FENCE_GATE:
		case JUNGLE_FENCE_GATE:
		case OAK_FENCE_GATE:
		case SPRUCE_FENCE_GATE:
		case LEVER:
		case REPEATER:
		case COMMAND_BLOCK:
		case COMPARATOR:
		case DAYLIGHT_DETECTOR:
		case CRAFTING_TABLE:
		case STONECUTTER:
		case JUKEBOX:
		case NOTE_BLOCK:
		case BELL:
		case GRINDSTONE:
		case LOOM:
		case FLETCHING_TABLE:
		case CARTOGRAPHY_TABLE:
		case SMITHING_TABLE:
		case ENCHANTING_TABLE:
		case BEACON:
		case ENDER_CHEST:
		case FLOWER_POT:
			return true;
		case LECTERN:
			Lectern lectern = (Lectern) block.getBlockData();
			return lectern.hasBook();
		default:
			return false;
		}
	}
}
