package com.piggest.minecraft.bukkit.pigman_switch;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Pigman_switch extends Multi_block_structure {

	@Override
	public void on_right_click(Player player) {
		return;
	}

	@Override
	public boolean create_condition(Player player) {
		return true;
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	@Override
	public Pigman_switch_manager get_manager() {
		return (Pigman_switch_manager) super.get_manager();
	}

	public boolean activated() {
		return this.get_location().getBlock().isBlockPowered();
	}

	@Override
	protected void init_after_set_location() {
		return;
	}

	@Override
	public ItemStack[] get_drop_items() {
		return null;
	}
}