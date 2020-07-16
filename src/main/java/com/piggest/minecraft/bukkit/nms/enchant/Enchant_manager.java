package com.piggest.minecraft.bukkit.nms.enchant;

import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public interface Enchant_manager {
	public void enchant(ItemStack itemstack, InventoryView view, EnchantmentOffer[] offer, int bonus);
}
