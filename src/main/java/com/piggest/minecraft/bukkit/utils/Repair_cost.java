package com.piggest.minecraft.bukkit.utils;

import org.bukkit.inventory.ItemStack;

public class Repair_cost {
	public static ItemStack setRepairCost(ItemStack item, Integer cost) {
		net.minecraft.server.v1_14_R1.ItemStack itemNms = org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack
				.asNMSCopy(item);
		net.minecraft.server.v1_14_R1.NBTTagCompound tag;
		if (itemNms.hasTag()) {
			tag = itemNms.getTag();
		} else {
			tag = new net.minecraft.server.v1_14_R1.NBTTagCompound();
		}
		if (cost != null) {
			tag.setInt("RepairCost", cost);
		} else {
			if (itemNms.getTag().hasKeyOfType("RepairCost", 3)) {
				tag.remove("RepairCost");
			}
		}
		itemNms.setTag(tag);
		return org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack.asBukkitCopy(itemNms);
	}

	public static Integer getRepairCost(ItemStack item) {
		net.minecraft.server.v1_14_R1.ItemStack itemNms = org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack
				.asNMSCopy(item);
		if (itemNms.hasTag()) {
			if (itemNms.getTag().hasKeyOfType("RepairCost", 3)) {
				return itemNms.getTag().getInt("RepairCost");
			} else {
				return null;
			}
		} else
			return null;
	}
}
