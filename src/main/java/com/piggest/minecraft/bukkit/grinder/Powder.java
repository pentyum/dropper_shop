package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Powder {
	public static ItemStack get_powder(Material material) {
		ItemStack item = new ItemStack(Material.SUGAR);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("粉末");
		item.setItemMeta(itemmeta);
		return item;
	}
}
