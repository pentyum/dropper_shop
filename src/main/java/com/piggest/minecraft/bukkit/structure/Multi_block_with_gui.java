package com.piggest.minecraft.bukkit.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;
import com.piggest.minecraft.bukkit.gui.Slot_config;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public abstract class Multi_block_with_gui extends Multi_block_structure implements InventoryHolder {
	protected Inventory gui;

	public Multi_block_with_gui() {
		if (this.get_manager().get_inventory_type() == InventoryType.CHEST) {
			this.gui = Bukkit.createInventory(this, this.get_manager().get_slot_num(),
					this.get_manager().get_gui_name());
		} else {
			this.gui = Bukkit.createInventory(this, this.get_manager().get_inventory_type(),
					this.get_manager().get_gui_name());
		}

		for (Entry<Integer, Slot_config> entry : this.get_manager().get_locked_slots().entrySet()) {
			int slot = entry.getKey();
			Slot_config slot_config = entry.getValue();
			if (slot_config.material != null || slot_config.full_name != null) {
				ItemStack item;
				if (slot_config.material != null) {
					item = new ItemStack(slot_config.material);
				} else {
					item = Material_ext.new_item_full_name(slot_config.full_name, 1);
				}
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(slot_config.name);
				if (slot_config.lore != null) {
					meta.setLore(new ArrayList<String>(Arrays.asList(slot_config.lore)));
				}
				item.setItemMeta(meta);
				this.getInventory().setItem(slot, item);
			}
		}
	}

	@Override
	protected void on_right_click(Player player) {
		if (player.hasPermission(this.get_manager().get_permission_head() + ".use")) {
			player.closeInventory();
			player.openInventory(this.getInventory());
			// if (this.is_loaded() == false) {
			// this.set_loaded(true);
			// }
		} else {
			player.sendMessage("你没有权限使用" + this.get_manager().get_gui_name());
		}
	}

	/*
	 * public void unpress_button(int i) { ItemStack item =
	 * this.getInventory().getItem(i); ItemMeta meta = item.getItemMeta();
	 * meta.setLore(null); item.setItemMeta(meta); }
	 * 
	 * public boolean pressed_button(int i) { ItemStack item =
	 * this.getInventory().getItem(i); ItemMeta meta = item.getItemMeta(); return
	 * meta.hasLore(); }
	 */
	@Override
	public Inventory getInventory() {
		return this.gui;
	}

	/*
	 * Button类型槽位点击时触发
	 */
	public abstract void on_button_pressed(Player player, int slot);

	/*
	 * Switch类型槽位点击时触发，on为要切换的状态，返回true表示允许切换
	 */
	public abstract boolean on_switch_pressed(Player player, int slot, boolean on);

	/*
	 * Switch类型槽位设置
	 */
	public void set_switch(int i, boolean value) {
		ItemStack item = this.gui.getItem(i);
		ItemMeta meta = item.getItemMeta();
		List<String> lore;
		if (meta.hasLore() == false) {
			lore = new ArrayList<String>();
			lore.add(value ? "§r当前: 开启" : "§r当前: 关闭");
		} else {
			lore = meta.getLore();
			lore.set(0, value ? "§r当前: 开启" : "§r当前: 关闭");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public boolean get_switch(int i) {
		ItemStack item = this.gui.getItem(i);
		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return false;
		}
		List<String> lore = meta.getLore();
		if (lore == null) {
			return false;
		}
		String info = lore.get(0);
		if (info.equals("§r当前: 开启")) {
			return true;
		} else {
			return false;
		}
	}

	public synchronized void set_process(int bar, int process, String info, Object... args) {
		int start_slot = -1;
		start_slot = this.get_manager().get_process_bar()[bar];
		int i = 0;
		int n = process * 9 / 100;
		for (i = start_slot; i < 9; i++) {
			ItemStack item = this.gui.getItem(i);
			if (Grinder.is_empty(item)) {
				item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
				this.gui.setItem(i, item);
			}
			if (i < n) {
				item.setType(Material.RED_STAINED_GLASS_PANE);
			} else {
				item.setType(Material.WHITE_STAINED_GLASS_PANE);
			}
			item = this.gui.getItem(i);
			ItemMeta meta = item.getItemMeta();
			// Bukkit.getServer().getLogger().info(info);
			meta.setDisplayName(String.format(info, args));
			item.setItemMeta(meta);
		}
	}

	@Override
	public Gui_structure_manager<? extends Multi_block_with_gui> get_manager() {
		return (Gui_structure_manager<?>) super.get_manager();
	}

	@Override
	public String get_display_name() {
		return this.get_manager().get_gui_name();
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		HashMap<Integer, Slot_config> locked_slots = this.get_manager().get_locked_slots();
		for (Entry<Integer, Slot_config> entry : locked_slots.entrySet()) {
			int slot = entry.getKey();
			Slot_config config = entry.getValue();
			if (config.type == Gui_slot_type.Item_store) {
				save.put(config.name, this.gui.getItem(slot));
			}
		}
		return save;
	}

	@Override
	protected void set_from_save(Map<String, Object> save) {
		super.set_from_save(save);
		HashMap<Integer, Slot_config> locked_slots = this.get_manager().get_locked_slots();
		for (Entry<Integer, Slot_config> entry : locked_slots.entrySet()) {
			int slot = entry.getKey();
			Slot_config config = entry.getValue();
			if (config.type == Gui_slot_type.Item_store) {
				if (save.get(config.name) != null) {
					this.gui.setItem(slot, (ItemStack) save.get(config.name));
				}
			}
		}
	}

	/*
	 * Item_store类型的槽位放物品时触发，返回true表示允许放入
	 */
	public abstract boolean on_put_item(Player player, ItemStack cursor_item, int slot);

	/*
	 * Item_store类型的槽位取出物品时触发，返回true表示允许放入
	 */
	public abstract boolean on_take_item(Player player, ItemStack in_item, int slot);

	/*
	 * Item_store类型的槽位替换物品时触发，返回true表示允许放入
	 */
	public abstract boolean on_exchange_item(Player player, ItemStack in_item, ItemStack cursor_item, int slot);
}
