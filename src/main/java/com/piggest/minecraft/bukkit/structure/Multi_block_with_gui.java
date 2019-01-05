package com.piggest.minecraft.bukkit.structure;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.gui.Gui_config;
import com.piggest.minecraft.bukkit.gui.Gui_runner;
import com.piggest.minecraft.bukkit.gui.Slot_config;

public abstract class Multi_block_with_gui extends Multi_block_structure implements InventoryHolder, HasRunner {
	protected Gui_runner gui_runner = new Gui_runner(this);
	protected Inventory gui;

	public Multi_block_with_gui() {
		if (this.get_gui_config().get_inventory_type() == InventoryType.CHEST) {
			this.gui = Bukkit.createInventory(this, this.get_gui_config().get_slot_num(),
					this.get_gui_config().get_gui_name());
		} else {
			this.gui = Bukkit.createInventory(this, this.get_gui_config().get_inventory_type(),
					this.get_gui_config().get_gui_name());
		}
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

	@Override
	public Inventory getInventory() {
		return this.gui;
	}

	public abstract void on_button_pressed(Player player, int slot);

	public void set_process(int bar, int process, String info, Object... args) {
		int start_slot = -1;
		try {
			start_slot = this.get_gui_config().get_process_bar()[bar];
		} catch (Exception e) {
			return;
		}
		int i = 0;
		int n = process * 9 / 100;
		ItemStack red = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemStack white = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta meta = red.getItemMeta();
		meta.setDisplayName(String.format(info, args));
		red.setItemMeta(meta);
		white.setItemMeta(meta);
		for (i = start_slot; i < n; i++) {
			this.gui.setItem(i, red.clone());
		}
		for (i = start_slot; i < 9; i++) {
			this.gui.setItem(i, white.clone());
		}
	}

}
