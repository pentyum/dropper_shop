package com.piggest.minecraft.bukkit.structure;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.grinder.Grinder;

public abstract class Multi_block_with_gui extends Multi_block_structure implements InventoryHolder, HasRunner {
	HashSet<Integer> locked_items = new HashSet<Integer>();

	@Override
	public void on_right_click(Player player) {
		player.closeInventory();
		player.openInventory(this.getInventory());
	}

	public void unpress_button(int i) {
		ItemStack item = this.getInventory().getItem(i);
		ItemMeta meta = item.getItemMeta();
		meta.setLore(null);
		item.setItemMeta(meta);
	}

	public boolean pressed_button(int i) {
		ItemStack item = this.getInventory().getItem(i);
		ItemMeta meta = item.getItemMeta();
		return meta.hasLore();
	}

	public void set_gui(int i, Material material, String name) {
		ItemStack item = new ItemStack(material);
		Grinder.set_item_name(item, name);
		this.getInventory().setItem(i, item);
		this.locked_items.add(i);
	}

}
