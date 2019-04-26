package com.piggest.minecraft.bukkit.lottery_pool;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Lottery_pool extends Multi_block_structure {
	EnderCrystal[] ender_crystal_list = new EnderCrystal[8];

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

	public void start_lottery(Player player) {
		for (EnderCrystal ender_crystal : ender_crystal_list) {
			if (ender_crystal == null) {
				player.sendMessage("末影水晶结构错误");
				return;
			}
		}
		if (!this.use_condition(player)) {
			return;
		}
		for (EnderCrystal ender_crystal : ender_crystal_list) { // 设置特效光柱
			ender_crystal.setBeamTarget(this.get_location());
		}
	}

	public void end_lottery(Player player) {
		FileConfiguration config = Dropper_shop_plugin.instance.get_lottery_config();
		ItemStack[] pool = new ItemStack[100];
		World world = this.get_location().getWorld();
		world.strikeLightning(this.get_location());
		for (EnderCrystal ender_crystal : ender_crystal_list) { // 设置特效光柱
			ender_crystal.setBeamTarget(null);
		}
		Set<String> item_types = config.getKeys(false);
		int i = 0, j = 0;
		for (String item_name : item_types) {
			List<Integer> item_info = config.getIntegerList(item_name);
			for (j = 0; j < item_info.get(1) && i < 100; j++) {
				pool[i] = Material_ext.new_item(item_name, item_info.get(0));
				i++;
			}
		}
		Random rand = new Random();
		int num = rand.nextInt(100);
		ItemStack item = pool[num];
		if (item != null) {
			player.sendMessage("恭喜你抽到了" + item.getAmount() + "个" + Material_ext.get_display_name(item));
			world.dropItem(this.get_location().add(0, 1, 0), item);
		} else {
			player.sendMessage("很遗憾你没有抽到任何物品");
		}
	}
}
