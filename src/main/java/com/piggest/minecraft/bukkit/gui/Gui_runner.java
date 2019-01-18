package com.piggest.minecraft.bukkit.gui;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Gui_runner extends Structure_runner {
	private Multi_block_with_gui multi_block_with_gui;

	public Gui_runner(Multi_block_with_gui multi_block_with_gui) {
		this.multi_block_with_gui = multi_block_with_gui;
	}

	@Override
	public void run() {
		for (Entry<Integer, Slot_config> entry : this.multi_block_with_gui.get_manager().get_locked_slots()
				.entrySet()) {
			int slot = entry.getKey();
			Slot_config config = entry.getValue();
			if (config.type == Gui_slot_type.Button) {
				if (this.multi_block_with_gui.pressed_button(slot) == true) {
					ItemStack item = multi_block_with_gui.getInventory().getItem(slot);
					ItemMeta meta = item.getItemMeta();
					List<String> lore = meta.getLore();
					Player player = Bukkit.getPlayer(lore.get(0));
					this.multi_block_with_gui.on_button_pressed(player, slot);
					this.multi_block_with_gui.unpress_button(slot);
				}
			}
		}
	}

	@Override
	public int get_cycle() {
		return 2;
	}

	@Override
	public int get_delay() {
		return 10;
	}

	@Override
	public boolean is_asynchronously() {
		return true;
	}

}
