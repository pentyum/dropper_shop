package com.piggest.minecraft.bukkit.nms.nbt;

import org.bukkit.inventory.ItemStack;

public interface Repair_cost {
	public ItemStack setRepairCost(ItemStack item, Integer cost);

	public Integer getRepairCost(ItemStack item);
}
