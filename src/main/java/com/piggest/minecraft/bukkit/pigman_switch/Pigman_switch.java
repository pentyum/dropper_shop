package com.piggest.minecraft.bukkit.pigman_switch;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Pigman_switch extends Multi_block_structure {

	@Override
	public void on_right_click(Player player) {
		String msg = "当前猪人生成控制器状态为: ";
		if (this.activated() == true) {
			msg += "开,3*3区块内的猪人不会生成";
		} else {
			msg += "关,猪人正常生成";
		}
		player.sendMessage(msg);
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