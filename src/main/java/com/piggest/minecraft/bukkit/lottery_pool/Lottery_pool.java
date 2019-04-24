package com.piggest.minecraft.bukkit.lottery_pool;

import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Lottery_pool extends Multi_block_structure {
	EnderCrystal[] ender_crystal = new EnderCrystal[8];

	@Override
	public void on_right_click(Player player) {
		return;
	}

	@Override
	public int completed() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public boolean in_structure(Location loc) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected boolean on_break(Player player) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean create_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_make_lottery_pool_price();
		if (Dropper_shop_plugin.instance.get_economy().has(player, price)) {
			Dropper_shop_plugin.instance.get_economy().withdrawPlayer(player, price);
			player.sendMessage("已扣除" + price);
			return true;
		} else {
			player.sendMessage("建立抽奖机的钱不够，需要" + price);
			return false;
		}
	}

	public boolean use_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_lottery_price();
		if (Dropper_shop_plugin.instance.get_economy().has(player, price)) {
			Dropper_shop_plugin.instance.get_economy().withdrawPlayer(player, price);
			player.sendMessage("已扣除" + price);
			return true;
		} else {
			player.sendMessage("抽奖钱不够，需要" + price);
			return false;
		}
	}

	public void luck(Player player) {

	}
}
