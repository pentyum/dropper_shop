package com.piggest.minecraft.bukkit.structure;

import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.gui.Gui_config;
import com.piggest.minecraft.bukkit.gui.Gui_runner;
import com.piggest.minecraft.bukkit.gui.Slot_config;

public abstract class Multi_block_with_gui extends Multi_block_structure implements InventoryHolder, HasRunner {
	protected Gui_runner gui_runner = new Gui_runner(this);

	public Multi_block_with_gui() {
		for (Entry<Integer, Slot_config> entry : this.get_gui_config().get_locked_slots().entrySet()) {
			int slot = entry.getKey();
			Slot_config slot_config = entry.getValue();
			ItemStack item = new ItemStack(slot_config.material);
			Grinder.set_item_name(item, slot_config.name);
			this.getInventory().setItem(slot, item);
		}
	}

	public abstract Gui_config get_gui_config();

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
}
