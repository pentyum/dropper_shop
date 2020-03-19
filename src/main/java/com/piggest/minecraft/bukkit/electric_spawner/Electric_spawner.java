package com.piggest.minecraft.bukkit.electric_spawner;

import java.util.HashSet;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;

public class Electric_spawner extends Multi_block_with_gui {
	public static final int info_indicator_slot = 9;
	public static final int synthesis_button_slot = 17;
	@Override
	public boolean completed() {
		boolean base_structure = super.completed();
		if (base_structure == false) {
			return false;
		}
		HashSet<Material> block_set = new HashSet<>();
		block_set.add(this.get_block(0, 2, 1).getType());
		block_set.add(this.get_block(0, 2, -1).getType());
		block_set.add(this.get_block(1, 2, 0).getType());
		block_set.add(this.get_block(-1, 2, 0).getType());
		if (!block_set.contains(Material.CREEPER_HEAD)) {
			return false;
		}
		if (!block_set.contains(Material.ZOMBIE_HEAD)) {
			return false;
		}
		if (!block_set.contains(Material.SKELETON_SKULL)) {
			return false;
		}
		if (!block_set.contains(Material.WITHER_SKELETON_SKULL)) {
			return false;
		}
		return true;
	}

	@Override
	public void on_button_pressed(Player player, int slot) {
		// TODO 自动生成的方法存根

	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean on_put_item(Player player, ItemStack cursor_item, int slot) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean on_take_item(Player player, ItemStack in_item, int slot) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean on_exchange_item(Player player, ItemStack in_item, ItemStack cursor_item, int slot) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean create_condition(Player player) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected void init_after_set_location() {
		// TODO 自动生成的方法存根

	}

	@Override
	protected boolean on_break(Player player) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public ItemStack[] get_drop_items() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Nonnull
	public static Electric_spawner deserialize(@Nonnull Map<String, Object> args) {
		Electric_spawner structure = new Electric_spawner();
		structure.set_from_save(args);
		return structure;
	}
}
