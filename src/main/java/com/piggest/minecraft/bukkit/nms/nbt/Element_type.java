package com.piggest.minecraft.bukkit.nms.nbt;

import org.bukkit.inventory.ItemStack;

public interface Element_type {
	public int get_element_id(ItemStack item);

	public boolean has_element_id(ItemStack item);

	public ItemStack set_element_id(ItemStack item, int element_id);
}
