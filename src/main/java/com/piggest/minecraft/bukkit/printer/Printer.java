package com.piggest.minecraft.bukkit.printer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;

public class Printer extends Multi_block_with_gui {

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

}