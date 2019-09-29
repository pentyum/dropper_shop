package com.piggest.minecraft.bukkit.nms.nbt;

import org.bukkit.inventory.ItemStack;

public interface Flying_time {
	public int get_flying_time(ItemStack item);

	public boolean has_flying_time(ItemStack item);

	public ItemStack set_flying_time(ItemStack item, int time);
}
